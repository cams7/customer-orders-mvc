package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_FIRSTNAME;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_LASTNAME;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_USERNAME;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerResponseTemplate {

  public static void loadTemplates() {
    of(CustomerResponse.class)
        .addTemplate(
            CUSTOMER_RESPONSE,
            new Rule() {
              {
                add("id", CUSTOMER_ID);
                add("firstName", CUSTOMER_FIRSTNAME);
                add("lastName", CUSTOMER_LASTNAME);
                add("username", CUSTOMER_USERNAME);
              }
            });
  }
}
