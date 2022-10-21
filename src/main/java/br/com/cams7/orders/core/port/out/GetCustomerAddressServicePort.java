package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface GetCustomerAddressServicePort {
  CompletableFuture<Optional<CustomerAddress>> getCustomerAddress(
      String country, String requestTraceId, String customerId, String postcode);
}
