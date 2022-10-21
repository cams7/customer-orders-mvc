package br.com.cams7.orders.adapter.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @Schema(example = "57a98d98e4b00679b4a830af", description = "Customer id")
  private String customerId;

  @Schema(example = "89816-170", description = "Address postcode")
  private String addressPostcode;

  @Schema(example = "5413096279109654", description = "Card number")
  private String cardNumber;

  @Schema(example = "5a934e000102030405000024", description = "Cart id")
  private String cartId;
}
