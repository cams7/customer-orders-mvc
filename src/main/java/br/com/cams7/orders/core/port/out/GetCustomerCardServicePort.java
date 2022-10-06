package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerCard;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface GetCustomerCardServicePort {
  CompletableFuture<CustomerCard> getCustomerCard(
      String country, String requestTraceId, String cardUrl);
}
