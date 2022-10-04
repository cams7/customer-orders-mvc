package br.com.cams7.orders.adapter.webclient.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingResponse {
  private String id;

  private String orderId;

  private String country;
}
