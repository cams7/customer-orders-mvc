package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.OrderEntity;

@FunctionalInterface
public interface CreateOrderRepositoryPort {
  OrderEntity create(String country, OrderEntity order);
}
