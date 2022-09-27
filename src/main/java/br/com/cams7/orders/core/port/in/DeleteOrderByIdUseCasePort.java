package br.com.cams7.orders.core.port.in;

@FunctionalInterface
public interface DeleteOrderByIdUseCasePort {
  void execute(String country, String orderId);
}
