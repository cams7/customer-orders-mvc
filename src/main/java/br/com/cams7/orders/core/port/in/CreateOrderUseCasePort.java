package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import java.util.Optional;

@FunctionalInterface
public interface CreateOrderUseCasePort {
  Optional<OrderEntity> execute(String country, String requestTraceId, CreateOrderCommand order);
}
