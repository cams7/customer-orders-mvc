package br.com.cams7.orders.adapter.webclient;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import br.com.cams7.orders.adapter.webclient.request.PaymentRequest;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.Payment;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService extends BaseWebclient implements VerifyPaymentServicePort {

  private final RestTemplate restTemplate;
  private final ModelMapper modelMapper;

  @Value("${api.paymentUrl}")
  private String paymentUrl;

  @Async
  @Override
  public CompletableFuture<Optional<Payment>> verify(
      final String country,
      final String requestTraceId,
      final String customerId,
      final Float amount) {
    final var payment =
        getPayment(
            restTemplate
                .exchange(
                    getRequest(
                        fromHttpUrl(paymentUrl).path("/payments").build().toUri(),
                        country,
                        requestTraceId,
                        new PaymentRequest(customerId, amount)),
                    PaymentResponse.class)
                .getBody());
    return CompletableFuture.completedFuture(payment);
  }

  private Optional<Payment> getPayment(final PaymentResponse response) {
    if (response == null) return Optional.empty();
    final var payment = modelMapper.map(response, Payment.class);
    return Optional.of(payment);
  }
}
