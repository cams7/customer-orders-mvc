package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerAddressTemplate {

  public static final String CUSTOMER_ADDRESS_ID = "57a98d98e4b00679b4a830ad";
  public static final String CUSTOMER_ADDRESS_NUMBER = "956";
  public static final String CUSTOMER_ADDRESS_STREET = "Rua Manoel Gregório Mattos";
  public static final String CUSTOMER_ADDRESS_POSTCODE = "89816-170";
  public static final String CUSTOMER_ADDRESS_CITY = "Chapecó";
  public static final String CUSTOMER_ADDRESS_FEDERATIVE_UNIT = "SC";
  public static final String CUSTOMER_ADDRESS_COUNTRY = "BR";

  public static void loadTemplates() {
    of(CustomerAddress.class)
        .addTemplate(
            CUSTOMER_ADDRESS,
            new Rule() {
              {
                add("addressId", CUSTOMER_ADDRESS_ID);
                add("number", CUSTOMER_ADDRESS_NUMBER);
                add("street", CUSTOMER_ADDRESS_STREET);
                add("postcode", CUSTOMER_ADDRESS_POSTCODE);
                add("city", CUSTOMER_ADDRESS_CITY);
                add("federativeUnit", CUSTOMER_ADDRESS_FEDERATIVE_UNIT);
                add("country", CUSTOMER_ADDRESS_COUNTRY);
              }
            });
  }
}
