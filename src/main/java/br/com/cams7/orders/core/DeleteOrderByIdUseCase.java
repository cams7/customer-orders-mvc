package br.com.cams7.orders.core;

import br.com.cams7.orders.core.port.in.DeleteOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteOrderByIdUseCase implements DeleteOrderByIdUseCasePort {

  private final DeleteOrderByIdRepositoryPort deleteOrderByIdRepository;

  @Override
  public Optional<Long> execute(String country, String orderId) {
    return deleteOrderByIdRepository.delete(country, orderId);
  }
}
