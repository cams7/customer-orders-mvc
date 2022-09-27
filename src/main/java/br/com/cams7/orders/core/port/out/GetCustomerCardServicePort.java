package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerCard;
import java.util.concurrent.Future;

@FunctionalInterface
public interface GetCustomerCardServicePort {
  Future<CustomerCard> getCustomerCard(String cardUrl);
}
