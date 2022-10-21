package br.com.cams7.orders.adapter.webclient;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService extends BaseWebclient
    implements GetCustomerServicePort, GetCustomerAddressServicePort, GetCustomerCardServicePort {

  private final RestTemplate restTemplate;
  private final ModelMapper modelMapper;

  @Value("${api.customerUrl}")
  private String customerUrl;

  @Value("${api.addressUrl}")
  private String addressUrl;

  @Value("${api.cardUrl}")
  private String cardUrl;

  @Async
  @Override
  public CompletableFuture<Optional<Customer>> getCustomer(
      final String country, final String requestTraceId, final String customerId) {
    final var customer =
        getCustomer(
            restTemplate
                .exchange(
                    getRequest(
                        fromHttpUrl(customerUrl).path("/customers/{id}").build(customerId),
                        country,
                        requestTraceId),
                    CustomerResponse.class)
                .getBody());
    return CompletableFuture.completedFuture(customer);
  }

  @Async
  @Override
  public CompletableFuture<Optional<CustomerAddress>> getCustomerAddress(
      final String country,
      final String requestTraceId,
      final String customerId,
      final String postcode) {
    final var customerAddress =
        getCustomerAddress(
            restTemplate
                .exchange(
                    getRequest(
                        fromHttpUrl(addressUrl)
                            .path("/addresses")
                            .queryParam("customerId", customerId)
                            .queryParam("postcode", postcode)
                            .build()
                            .toUri(),
                        country,
                        requestTraceId),
                    new ParameterizedTypeReference<List<CustomerAddressResponse>>() {})
                .getBody()
                .stream()
                .findFirst());
    return CompletableFuture.completedFuture(customerAddress);
  }

  @Async
  @Override
  public CompletableFuture<Optional<CustomerCard>> getCustomerCard(
      final String country,
      final String requestTraceId,
      final String customerId,
      final String longNum) {
    final var customerCard =
        getCustomerCard(
            restTemplate
                .exchange(
                    getRequest(
                        fromHttpUrl(cardUrl)
                            .path("/cards")
                            .queryParam("customerId", customerId)
                            .queryParam("longNum", longNum)
                            .build()
                            .toUri(),
                        country,
                        requestTraceId),
                    new ParameterizedTypeReference<List<CustomerCardResponse>>() {})
                .getBody()
                .stream()
                .findFirst());
    return CompletableFuture.completedFuture(customerCard);
  }

  private Optional<Customer> getCustomer(final CustomerResponse response) {
    return Optional.ofNullable(response)
        .map(
            customer -> {
              final var entity = modelMapper.map(customer, Customer.class);
              entity.setCustomerId(customer.getId());
              return entity;
            });
  }

  private Optional<CustomerAddress> getCustomerAddress(
      final Optional<CustomerAddressResponse> response) {
    return response.map(
        customerAddress -> {
          final var entity = modelMapper.map(customerAddress, CustomerAddress.class);
          entity.setAddressId(customerAddress.getId());
          return entity;
        });
  }

  private Optional<CustomerCard> getCustomerCard(final Optional<CustomerCardResponse> response) {
    return response.map(
        customerCard -> {
          final var entity = modelMapper.map(customerCard, CustomerCard.class);
          entity.setCardId(customerCard.getId());
          return entity;
        });
  }
}
