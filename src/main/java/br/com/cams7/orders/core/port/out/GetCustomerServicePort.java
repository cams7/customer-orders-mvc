package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Customer;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface GetCustomerServicePort {
  CompletableFuture<Optional<Customer>> getCustomer(
      String country, String requestTraceId, String customerId);
}
