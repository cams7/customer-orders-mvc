package br.com.cams7.orders.template;

import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_MODEL;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_CITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_FEDERATIVE_UNIT;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_ID;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_NUMBER;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_POSTCODE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_STREET;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_ID;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_LONG_NUM;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_FULLNAME;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_USERNAME;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.AUTHORISED_TOTAL_AMOUNT;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.REGISTERED_SHIPPING;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.REGISTRATION_DATE;
import static br.com.six2six.fixturefactory.Fixture.of;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.repository.model.CartItem;
import br.com.cams7.orders.adapter.repository.model.Customer;
import br.com.cams7.orders.adapter.repository.model.CustomerAddress;
import br.com.cams7.orders.adapter.repository.model.CustomerCard;
import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.six2six.fixturefactory.Rule;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class OrderModelTemplate {

  public static void loadTemplates() {
    of(OrderModel.class)
        .addTemplate(
            ORDER_MODEL,
            new Rule() {
              {
                add("id", ORDER_ID);
                add("customer", new Customer(CUSTOMER_ID, CUSTOMER_FULLNAME, CUSTOMER_USERNAME));
                add(
                    "address",
                    new CustomerAddress(
                        CUSTOMER_ADDRESS_ID,
                        CUSTOMER_ADDRESS_NUMBER,
                        CUSTOMER_ADDRESS_STREET,
                        CUSTOMER_ADDRESS_POSTCODE,
                        CUSTOMER_ADDRESS_CITY,
                        CUSTOMER_ADDRESS_FEDERATIVE_UNIT,
                        CUSTOMER_ADDRESS_COUNTRY));
                add("card", new CustomerCard(CUSTOMER_CARD_ID, CUSTOMER_CARD_LONG_NUM));
                add(
                    "items",
                    List.of(
                        new CartItem(
                            CART_ITEM1_PRODUCT_ID, CART_ITEM1_QUANTITY, CART_ITEM1_UNIT_PRICE),
                        new CartItem(
                            CART_ITEM3_PRODUCT_ID, CART_ITEM3_QUANTITY, CART_ITEM3_UNIT_PRICE)));
                add("registrationDate", LocalDateTime.parse(REGISTRATION_DATE, ISO_DATE_TIME));
                add("total", AUTHORISED_TOTAL_AMOUNT);
                add("registeredShipping", REGISTERED_SHIPPING);
              }
            });
  }
}
