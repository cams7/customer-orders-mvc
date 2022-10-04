package br.com.cams7.orders.core.port.out;

@FunctionalInterface
public interface UpdateShippingByIdRepositoryPort {
  Long updateShipping(String country, String orderId, Boolean registeredShipping);
}
