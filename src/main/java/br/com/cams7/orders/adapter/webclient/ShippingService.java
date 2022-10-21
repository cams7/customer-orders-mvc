package br.com.cams7.orders.adapter.webclient;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import br.com.cams7.orders.adapter.webclient.request.ShippingRequest;
import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ShippingService extends BaseWebclient implements AddShippingOrderServicePort {

  private final RestTemplate restTemplate;

  @Value("${api.shippingUrl}")
  private String shippingUrl;

  @Async
  @Override
  public CompletableFuture<Optional<String>> add(
      final String country, final String requestTraceId, final String orderId) {
    final var shippingId =
        getShippingId(
            restTemplate
                .exchange(
                    getRequest(
                        fromHttpUrl(shippingUrl).path("/shippings").build().toUri(),
                        country,
                        requestTraceId,
                        new ShippingRequest(orderId)),
                    ShippingResponse.class)
                .getBody());
    return CompletableFuture.completedFuture(shippingId);
  }

  private Optional<String> getShippingId(final ShippingResponse response) {
    return Optional.ofNullable(response)
        .map(
            shipping -> {
              final var shippingId = shipping.getId();
              return shippingId;
            });
  }
}
