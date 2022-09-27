package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Payment;
import java.util.concurrent.Future;

@FunctionalInterface
public interface VerifyPaymentServicePort {
  Future<Payment> verify(String customerId, Float amount);
}
