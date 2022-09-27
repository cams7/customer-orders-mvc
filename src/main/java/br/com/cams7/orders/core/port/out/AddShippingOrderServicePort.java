package br.com.cams7.orders.core.port.out;

import java.util.concurrent.Future;

@FunctionalInterface
public interface AddShippingOrderServicePort {
  Future<String> add(String orderId);
}
