package br.com.cams7.orders.core.port.out;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface AddShippingOrderServicePort {
  CompletableFuture<Optional<String>> add(String country, String requestTraceId, String orderId);
}
