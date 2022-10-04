package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerAddress;
import java.util.concurrent.Future;

@FunctionalInterface
public interface GetCustomerAddressServicePort {
  Future<CustomerAddress> getCustomerAddress(
      String country, String requestTraceId, String addressUrl);
}
