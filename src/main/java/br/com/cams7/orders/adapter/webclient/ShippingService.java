package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.request.ShippingRequest;
import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
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
  public CompletableFuture<String> add(String country, String requestTraceId, String orderId) {
    var shippingId =
        restTemplate
            .exchange(
                getRequest(shippingUrl, country, requestTraceId, new ShippingRequest(orderId)),
                ShippingResponse.class)
            .getBody()
            .getId();
    return CompletableFuture.completedFuture(shippingId);
  }
}
