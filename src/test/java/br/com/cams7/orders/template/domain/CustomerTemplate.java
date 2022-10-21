package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.Customer;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerTemplate {

  public static final String CUSTOMER_ID = "57a98d98e4b00679b4a830af";
  public static final String CUSTOMER_FIRSTNAME = "Gustavo";
  public static final String CUSTOMER_LASTNAME = "Ramos";
  public static final String CUSTOMER_FULLNAME =
      String.format("%s %s", CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME);
  public static final String CUSTOMER_USERNAME = "gustavo";

  public static void loadTemplates() {
    of(Customer.class)
        .addTemplate(
            CUSTOMER,
            new Rule() {
              {
                add("customerId", CUSTOMER_ID);
                add("firstName", CUSTOMER_FIRSTNAME);
                add("lastName", CUSTOMER_LASTNAME);
                add("fullName", CUSTOMER_FULLNAME);
                add("username", CUSTOMER_USERNAME);
              }
            });
  }
}
