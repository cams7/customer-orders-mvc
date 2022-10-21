package br.com.cams7.orders.core;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.regex.Pattern.matches;

import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.CreateOrderUseCasePort;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderUseCasePort {

  private static final String ID_REGEX = "^[\\w\\-]+$";

  private final Integer timeoutInSeconds;
  private final Float shippingAmount;
  private final GetCustomerServicePort getCustomerService;
  private final GetCustomerAddressServicePort getCustomerAddressService;
  private final GetCustomerCardServicePort getCustomerCardService;
  private final GetCartItemsServicePort getCartItemsService;
  private final VerifyPaymentServicePort verifyPaymentService;
  private final AddShippingOrderServicePort addShippingOrderService;
  private final CreateOrderRepositoryPort createOrderRepository;
  private final UpdateShippingByIdRepositoryPort updateShippingByIdRepository;

  @Override
  public Optional<OrderEntity> execute(
      final String country, final String requestTraceId, final CreateOrderCommand command) {
    return getCustomer(country, requestTraceId, command.getCustomerId())
        .map(CreateOrderUseCase::getOrderWithCustomer)
        .map(
            order ->
                getOrderWithAddressAndCardAndSortedItems(
                    order,
                    country,
                    requestTraceId,
                    command.getAddressPostcode(),
                    command.getCardNumber(),
                    command.getCartId()))
        .flatMap(order -> verifyPayment(country, requestTraceId, order))
        .flatMap(order -> createOrder(country, order))
        .flatMap(order -> addShippingOrder(country, requestTraceId, order))
        .flatMap(
            shippingAndOrder ->
                updateShipping(country, shippingAndOrder.shippingId, shippingAndOrder.order));
  }

  private Optional<Customer> getCustomer(
      final String country, final String requestTraceId, final String customerId) {
    final var customerFuture = getCustomerService.getCustomer(country, requestTraceId, customerId);
    try {
      final var customer = customerFuture.get(timeoutInSeconds, SECONDS);
      return customer;
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new ResponseStatusException(e.getMessage(), INTERNAL_SERVER_ERROR_CODE);
    }
  }

  private static OrderEntity getOrderWithCustomer(final Customer customer) {
    customer.setFullName(String.format("%s %s", customer.getFirstName(), customer.getLastName()));
    return new OrderEntity().withCustomer(customer);
  }

  private OrderEntity getOrderWithAddressAndCardAndSortedItems(
      final OrderEntity order,
      final String country,
      final String requestTraceId,
      final String addressPostcode,
      final String cardNumber,
      final String cartId) {
    final var customerId = order.getCustomer().getCustomerId();

    final var addressFuture =
        getCustomerAddressService.getCustomerAddress(
            country, requestTraceId, customerId, addressPostcode);
    final var cardFuture =
        getCustomerCardService.getCustomerCard(country, requestTraceId, customerId, cardNumber);

    final var itemsFuture =
        getCartItemsService.getCartItems(country, requestTraceId, customerId, cartId);

    CompletableFuture.allOf(addressFuture, cardFuture, itemsFuture).join();

    try {
      final var address = addressFuture.get(timeoutInSeconds, SECONDS).orElse(null);
      final var card = cardFuture.get(timeoutInSeconds, SECONDS).orElse(null);
      final var items =
          itemsFuture.get(timeoutInSeconds, SECONDS).parallelStream()
              .sorted(CreateOrderUseCase::compare)
              .collect(Collectors.toList());

      if (CollectionUtils.isEmpty(items))
        throw new ResponseStatusException("There aren't items in the cart", BAD_REQUEST_CODE);

      return order.withAddress(address).withCard(card).withItems(items);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new ResponseStatusException(e.getMessage(), INTERNAL_SERVER_ERROR_CODE);
    }
  }

  private Optional<OrderEntity> verifyPayment(
      final String country, final String requestTraceId, final OrderEntity order) {
    final var customerId = order.getCustomer().getCustomerId();
    final var totalAmount = getTotalAmount(order);

    final var paymentFuture =
        verifyPaymentService.verify(country, requestTraceId, customerId, totalAmount);

    try {
      return paymentFuture
          .get(timeoutInSeconds, SECONDS)
          .map(
              payment -> {
                if (!payment.isAuthorised())
                  throw new ResponseStatusException(payment.getMessage(), BAD_REQUEST_CODE);
                return order.withTotalAmount(totalAmount).withPayment(payment);
              });
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new ResponseStatusException(e.getMessage(), INTERNAL_SERVER_ERROR_CODE);
    }
  }

  private Optional<OrderEntity> createOrder(final String country, final OrderEntity order) {
    order.setRegistrationDate(ZonedDateTime.now());
    return createOrderRepository.create(country, order);
  }

  private Optional<ShippingAndOrder> addShippingOrder(
      final String country, final String requestTraceId, final OrderEntity order) {
    final var orderId = order.getOrderId();
    final var shippingFuture = addShippingOrderService.add(country, requestTraceId, orderId);

    try {
      return shippingFuture
          .get(timeoutInSeconds, SECONDS)
          .map(shippingId -> new ShippingAndOrder(shippingId, order));
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new ResponseStatusException(e.getMessage(), INTERNAL_SERVER_ERROR_CODE);
    }
  }

  private Optional<OrderEntity> updateShipping(
      final String country, final String shippingId, final OrderEntity order) {
    final var orderId = order.getOrderId();
    final var isRegisteredShipping = shippingId != null && matches(ID_REGEX, shippingId);
    return updateShippingByIdRepository
        .updateShipping(country, orderId, isRegisteredShipping)
        .map(
            modifiedCount -> {
              if (modifiedCount == null || modifiedCount <= 0)
                throw new ResponseStatusException(
                    String.format(
                        "The registeredShipping field of order %s hasn't been changed", orderId),
                    INTERNAL_SERVER_ERROR_CODE);

              return order.withRegisteredShipping(isRegisteredShipping);
            });
  }

  private float getTotalAmount(OrderEntity order) {
    return (float)
        (order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum()
            + shippingAmount);
  }

  private static int compare(CartItem item1, CartItem item2) {
    return compare(
        item2.getQuantity() * item2.getUnitPrice(), item1.getQuantity() * item1.getUnitPrice());
  }

  private static int compare(float value1, float value2) {
    if (value1 > value2) return 1;
    if (value1 < value2) return -1;
    return 0;
  }

  @AllArgsConstructor
  private static class ShippingAndOrder {
    private String shippingId;
    private OrderEntity order;
  }
}
