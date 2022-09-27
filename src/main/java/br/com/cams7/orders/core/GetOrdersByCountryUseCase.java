package br.com.cams7.orders.core;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrdersByCountryUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetOrdersByCountryUseCase implements GetOrdersByCountryUseCasePort {

  private final GetOrdersByCountryRepositoryPort getOrdersRepository;

  @Override
  public List<OrderEntity> execute(String country) {
    return getOrdersRepository.getOrders(country);
  }
}
