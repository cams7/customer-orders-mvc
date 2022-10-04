package br.com.cams7.orders.core.domain;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

  private String orderId;

  private Customer customer;

  private CustomerAddress address;

  private CustomerCard card;

  private List<CartItem> items;

  private ZonedDateTime registrationDate;

  private Float totalAmount;

  private Payment payment;

  private Boolean registeredShipping;
}
