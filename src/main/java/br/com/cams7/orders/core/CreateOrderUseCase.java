package br.com.cams7.orders.core;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.regex.Pattern.matches;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
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
  public OrderEntity execute(String country, String requestTraceId, CreateOrderCommand command) {
    try {
      var customerFuture =
          getCustomerService.getCustomer(country, requestTraceId, command.getCustomerUrl());
      var addressFuture =
          getCustomerAddressService.getCustomerAddress(
              country, requestTraceId, command.getAddressUrl());
      var cardFuture =
          getCustomerCardService.getCustomerCard(country, requestTraceId, command.getCardUrl());

      CompletableFuture.allOf(customerFuture, addressFuture, cardFuture).join();

      var customer = customerFuture.get(timeoutInSeconds, SECONDS);
      var address = addressFuture.get(timeoutInSeconds, SECONDS);
      var card = cardFuture.get(timeoutInSeconds, SECONDS);

      var order = new OrderEntity();
      order.setCustomer(customer);
      order.setAddress(address);
      order.setCard(card);

      var itemsFuture =
          getCartItemsService.getCartItems(country, requestTraceId, command.getItemsUrl());

      var items = itemsFuture.get(timeoutInSeconds, SECONDS);

      order.setItems(items);

      order = verifyCartItems(order);

      order = verifyPayment(country, requestTraceId, order);

      order = createOrder(country, order);

      var shippingAndOrder = addShippingOrder(country, requestTraceId, order);

      order = updateShipping(country, shippingAndOrder.shippingId, shippingAndOrder.order);

      return order;
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new ResponseStatusException(e.getMessage(), INTERNAL_SERVER_ERROR_CODE);
    }
  }

  private OrderEntity verifyCartItems(OrderEntity order) {
    if (CollectionUtils.isEmpty(order.getItems())) {
      throw new ResponseStatusException("There aren't items in the cart", BAD_REQUEST_CODE);
    }
    return order;
  }

  private OrderEntity verifyPayment(String country, String requestTraceId, OrderEntity order)
      throws InterruptedException, ExecutionException, TimeoutException {
    var customerId = order.getCustomer().getCustomerId();
    var totalAmount = getTotalAmount(order);
    var paymentFuture =
        verifyPaymentService.verify(country, requestTraceId, customerId, totalAmount);
    var payment = paymentFuture.get(timeoutInSeconds, SECONDS);

    return order.withTotalAmount(totalAmount).withPayment(payment);
  }

  private OrderEntity createOrder(String country, OrderEntity order) {
    var payment = order.getPayment();
    if (!payment.isAuthorised()) {
      throw new ResponseStatusException(payment.getMessage(), BAD_REQUEST_CODE);
    }
    order.setRegistrationDate(ZonedDateTime.now());
    return createOrderRepository.create(country, order);
  }

  private ShippingAndOrder addShippingOrder(
      String country, String requestTraceId, OrderEntity order)
      throws InterruptedException, ExecutionException, TimeoutException {
    var shippingFuture = addShippingOrderService.add(country, requestTraceId, order.getOrderId());
    var shippingId = shippingFuture.get(timeoutInSeconds, SECONDS);
    return new ShippingAndOrder(shippingId, order);
  }

  private OrderEntity updateShipping(String country, String shippingId, OrderEntity order) {
    var orderId = order.getOrderId();
    var isRegisteredShipping = shippingId != null && matches(ID_REGEX, shippingId);
    var modifiedCount =
        updateShippingByIdRepository.updateShipping(country, orderId, isRegisteredShipping);

    if (modifiedCount != null && modifiedCount > 0) {
      return order.withRegisteredShipping(isRegisteredShipping);
    }

    throw new ResponseStatusException(
        String.format("The registeredShipping field of order %s hasn't been changed", orderId),
        INTERNAL_SERVER_ERROR_CODE);
  }

  private float getTotalAmount(OrderEntity order) {
    return (float)
        (order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum()
            + shippingAmount);
  }

  @AllArgsConstructor
  private static class ShippingAndOrder {
    private String shippingId;
    private OrderEntity order;
  }
}
