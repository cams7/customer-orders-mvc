package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Payment;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface VerifyPaymentServicePort {
  CompletableFuture<Optional<Payment>> verify(
      String country, String requestTraceId, String customerId, Float amount);
}
