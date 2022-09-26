package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CartService extends BaseWebclient implements GetCartItemsServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Override
  public Flux<CartItem> getCartItems(String itemsUrl) {
    return getWebClient(builder, itemsUrl)
        .get()
        .header(COUNTRY_HEADER, getCountry())
        .header(REQUEST_TRACE_ID_HEADER, getRequestTraceId())
        .retrieve()
        .bodyToFlux(CartItemResponse.class)
        .map(this::getCartItem);
  }

  private CartItem getCartItem(CartItemResponse response) {
    return modelMapper.map(response, CartItem.class);
  }
}