package br.com.cams7.orders.core.port.out;

import java.util.Optional;

@FunctionalInterface
public interface DeleteOrderByIdRepositoryPort {
  Optional<Long> delete(String country, String orderId);
}
