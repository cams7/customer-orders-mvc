package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CartItem;
import java.util.List;
import java.util.concurrent.Future;

@FunctionalInterface
public interface GetCartItemsServicePort {
  Future<List<CartItem>> getCartItems(String country, String requestTraceId, String itemsUrl);
}
