package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;

@FunctionalInterface
public interface CreateOrderUseCasePort {
  OrderEntity execute(String country, String requestTraceId, CreateOrderCommand order);
}
