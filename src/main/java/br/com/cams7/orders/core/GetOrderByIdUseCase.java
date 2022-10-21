package br.com.cams7.orders.core;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetOrderByIdUseCase implements GetOrderByIdUseCasePort {

  private final GetOrderByIdRepositoryPort getOrderByIdRepository;

  @Override
  public Optional<OrderEntity> execute(String country, String orderId) {
    return getOrderByIdRepository.getOrder(country, orderId);
  }
}
