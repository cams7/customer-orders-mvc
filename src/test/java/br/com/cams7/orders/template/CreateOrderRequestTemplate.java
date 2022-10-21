package br.com.cams7.orders.template;

import static br.com.cams7.orders.template.DomainTemplateLoader.INVALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_POSTCODE;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_LONG_NUM;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CreateOrderRequestTemplate {

  public static final String CART_ID = "5a934e000102030405000024";
  public static final String INVALID_VALUE = "@bc";

  public static void loadTemplates() {
    of(CreateOrderRequest.class)
        .addTemplate(
            VALID_CREATE_ORDER_REQUEST,
            new Rule() {
              {
                add("customerId", CUSTOMER_ID);
                add("addressPostcode", CUSTOMER_ADDRESS_POSTCODE);
                add("cardNumber", CUSTOMER_CARD_LONG_NUM);
                add("cartId", CART_ID);
              }
            })
        .addTemplate(INVALID_CREATE_ORDER_REQUEST)
        .inherits(
            VALID_CREATE_ORDER_REQUEST,
            new Rule() {
              {
                add("customerId", INVALID_VALUE);
              }
            });
  }
}
