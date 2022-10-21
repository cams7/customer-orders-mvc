package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_ID;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_LONG_NUM;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerCardResponseTemplate {

  public static final String CUSTOMER_CARD_EXPIRES = "04/2023";
  public static final String CUSTOMER_CARD_CCV = "320";

  public static void loadTemplates() {
    of(CustomerCardResponse.class)
        .addTemplate(
            CUSTOMER_CARD_RESPONSE,
            new Rule() {
              {
                add("id", CUSTOMER_CARD_ID);
                add("longNum", CUSTOMER_CARD_LONG_NUM);
                add("expires", CUSTOMER_CARD_EXPIRES);
                add("ccv", CUSTOMER_CARD_CCV);
              }
            });
  }
}
