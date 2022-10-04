package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Customer;
import java.util.concurrent.Future;

@FunctionalInterface
public interface GetCustomerServicePort {
  Future<Customer> getCustomer(String country, String requestTraceId, String customerUrl);
}
