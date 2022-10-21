package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import java.util.Optional;

@FunctionalInterface
public interface GetOrderByIdUseCasePort {
  Optional<OrderEntity> execute(String country, String orderId);
}
