package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.OrderEntity;
import java.util.List;

@FunctionalInterface
public interface GetOrdersByCountryRepositoryPort {
  List<OrderEntity> getOrders(String country);
}
