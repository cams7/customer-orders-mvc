package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerAddress;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface GetCustomerAddressServicePort {
  CompletableFuture<CustomerAddress> getCustomerAddress(
      String country, String requestTraceId, String addressUrl);
}
