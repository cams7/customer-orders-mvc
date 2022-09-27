package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CartService extends BaseWebclient implements GetCartItemsServicePort {

  private final RestTemplate restTemplate;
  private final ModelMapper modelMapper;

  @Override
  public Future<List<CartItem>> getCartItems(String itemsUrl) {
    var cartItems =
        restTemplate
            .exchange(
                getRequest(itemsUrl), new ParameterizedTypeReference<List<CartItemResponse>>() {})
            .getBody()
            .stream()
            .map(this::getCartItem)
            .collect(Collectors.toList());
    return new AsyncResult<>(cartItems);
  }

  private CartItem getCartItem(CartItemResponse response) {
    return modelMapper.map(response, CartItem.class);
  }
}
