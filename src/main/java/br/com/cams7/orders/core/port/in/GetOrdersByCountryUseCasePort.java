package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import java.util.List;

@FunctionalInterface
public interface GetOrdersByCountryUseCasePort {
  List<OrderEntity> execute(String country);
}
