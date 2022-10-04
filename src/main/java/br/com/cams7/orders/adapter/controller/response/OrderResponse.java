package br.com.cams7.orders.adapter.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  @Schema(example = "57a98d98e4b00679b4a830af", description = "Order id")
  private String orderId;

  @Schema(description = "Customer who placed the order")
  private Customer customer;

  @Schema(description = "Customer address")
  private CustomerAddress address;

  @Schema(description = "Customer card")
  private CustomerCard card;

  @Schema(description = "Cart items")
  private List<CartItem> items;

  @Schema(example = "2022-09-22T17:46:53+00:00", description = "order registration date")
  private String registrationDate;

  @Schema(example = "64.55", description = "Total order amount")
  private Float totalAmount;

  @Schema(example = "true", description = "Shipping has registered")
  private Boolean registeredShipping;
}
