package br.com.cams7.orders.core.port.out;

@FunctionalInterface
public interface DeleteOrderByIdRepositoryPort {
  Long delete(String country, String orderId);
}
