package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerCardTemplate {

  public static final String CUSTOMER_CARD_ID = "57a98d98e4b00679b4a830ae";
  public static final String CUSTOMER_CARD_LONG_NUM = "5413096279109654";

  public static void loadTemplates() {
    of(CustomerCard.class)
        .addTemplate(
            CUSTOMER_CARD,
            new Rule() {
              {
                add("cardId", CUSTOMER_CARD_ID);
                add("longNum", CUSTOMER_CARD_LONG_NUM);
              }
            });
  }
}
