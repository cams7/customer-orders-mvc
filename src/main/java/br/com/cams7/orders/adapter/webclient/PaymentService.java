package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.request.PaymentRequest;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.Payment;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
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
  public CompletableFuture<Payment> verify(
      String country, String requestTraceId, String customerId, Float amount) {
    var payment =
        getPayment(
            restTemplate
                .exchange(
                    getRequest(
                        paymentUrl,
                        country,
                        requestTraceId,
                        new PaymentRequest(customerId, amount)),
                    PaymentResponse.class)
                .getBody());
    return CompletableFuture.completedFuture(payment);
  }

  private Payment getPayment(PaymentResponse response) {
    return modelMapper.map(response, Payment.class);
  }
}
