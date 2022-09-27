package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;

@FunctionalInterface
public interface GetOrderByIdUseCasePort {
  OrderEntity execute(String country, String orderId);
}
