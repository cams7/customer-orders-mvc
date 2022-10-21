package br.com.cams7.orders.core.port.out;

import java.util.Optional;

@FunctionalInterface
public interface UpdateShippingByIdRepositoryPort {
  Optional<Long> updateShipping(String country, String orderId, Boolean registeredShipping);
}
