package br.com.cams7.orders.core.port.in;

import java.util.Optional;

@FunctionalInterface
public interface DeleteOrderByIdUseCasePort {
  Optional<Long> execute(String country, String orderId);
}
