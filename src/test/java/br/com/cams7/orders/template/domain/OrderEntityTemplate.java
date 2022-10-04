package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT;
import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.Fixture.of;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.domain.Payment;
import br.com.six2six.fixturefactory.Rule;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class OrderEntityTemplate {

  public static final String ORDER_ID = "57a98d98e4b00679b4a830af";
  public static final String REGISTRATION_DATE = "2022-09-22T17:46:53";

  public static final float DECLINED_TOTAL_AMOUNT = 161.5f;
  public static final float AUTHORISED_TOTAL_AMOUNT = 91.5f;

  public static void loadTemplates() {
    of(OrderEntity.class)
        .addTemplate(
            DECLINED_ORDER_ENTITY,
            new Rule() {
              {
                add("orderId", ORDER_ID);
                add("customer", (Customer) from(Customer.class).gimme(CUSTOMER));
                add(
                    "address",
                    (CustomerAddress) from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS));
                add("card", (CustomerCard) from(CustomerCard.class).gimme(CUSTOMER_CARD));
                add(
                    "items",
                    List.of(
                        (CartItem) from(CartItem.class).gimme(CART_ITEM1),
                        (CartItem) from(CartItem.class).gimme(CART_ITEM2),
                        (CartItem) from(CartItem.class).gimme(CART_ITEM3)));
                add(
                    "registrationDate",
                    ZonedDateTime.of(LocalDateTime.parse(REGISTRATION_DATE, ISO_DATE_TIME), UTC));
                add("totalAmount", DECLINED_TOTAL_AMOUNT);
                add("payment", (Payment) from(Payment.class).gimme(DECLINED_PAYMENT));
                add("registeredShipping", null);
              }
            })
        .addTemplate(AUTHORISED_ORDER_ENTITY)
        .inherits(
            DECLINED_ORDER_ENTITY,
            new Rule() {
              {
                add(
                    "items",
                    List.of(
                        (CartItem) from(CartItem.class).gimme(CART_ITEM1),
                        (CartItem) from(CartItem.class).gimme(CART_ITEM3)));
                add("totalAmount", AUTHORISED_TOTAL_AMOUNT);
                add("payment", (Payment) from(Payment.class).gimme(AUTHORISED_PAYMENT));
                add("registeredShipping", true);
              }
            });
  }
}
