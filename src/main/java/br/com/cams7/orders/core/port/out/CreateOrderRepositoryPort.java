package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.OrderEntity;
import java.util.Optional;

@FunctionalInterface
public interface CreateOrderRepositoryPort {
  Optional<OrderEntity> create(String country, OrderEntity order);
}
