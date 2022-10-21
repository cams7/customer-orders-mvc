package br.com.cams7.orders.adapter.webclient;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CartService extends BaseWebclient implements GetCartItemsServicePort {

  private final RestTemplate restTemplate;
  private final ModelMapper modelMapper;

  @Value("${api.cartUrl}")
  private String cartUrl;

  @Async
  @Override
  public CompletableFuture<List<CartItem>> getCartItems(
      final String country,
      final String requestTraceId,
      final String customerId,
      final String cartId) {
    final var cartItems =
        restTemplate
            .exchange(
                getRequest(
                    fromHttpUrl(cartUrl)
                        .path("/items")
                        .queryParam("customerId", customerId)
                        .queryParam("cartId", cartId)
                        .build()
                        .toUri(),
                    country,
                    requestTraceId),
                new ParameterizedTypeReference<List<CartItemResponse>>() {})
            .getBody()
            .stream()
            .map(this::getCartItem)
            .collect(Collectors.toList());
    return CompletableFuture.completedFuture(cartItems);
  }

  private CartItem getCartItem(final CartItemResponse response) {
    return modelMapper.map(response, CartItem.class);
  }
}
