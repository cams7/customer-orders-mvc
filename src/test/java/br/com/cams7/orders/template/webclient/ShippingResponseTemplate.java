package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.SHIPPING_RESPONSE;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ShippingResponseTemplate {

  public static final String SHIPPING_ID = "6315da6f-5ab3-4262-968c-489ee541693f";

  public static void loadTemplates() {
    of(ShippingResponse.class)
        .addTemplate(
            SHIPPING_RESPONSE,
            new Rule() {
              {
                add("id", SHIPPING_ID);
                add("orderId", ORDER_ID);
              }
            });
  }
}