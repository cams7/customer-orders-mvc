package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_UNIT_PRICE;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CartItemResponseTemplate {

  public static final String CART_ITEM1_ID = "5a934e000102030405000025";
  public static final String CART_ITEM2_ID = "5a934e000102030405000026";
  public static final String CART_ITEM3_ID = "5a934e000102030405000027";

  public static void loadTemplates() {
    of(CartItemResponse.class)
        .addTemplate(
            CART_ITEM_RESPONSE1,
            new Rule() {
              {
                add("id", CART_ITEM1_ID);
                add("productId", CART_ITEM1_PRODUCT_ID);
                add("quantity", CART_ITEM1_QUANTITY);
                add("unitPrice", CART_ITEM1_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM_RESPONSE2,
            new Rule() {
              {
                add("id", CART_ITEM2_ID);
                add("productId", CART_ITEM2_PRODUCT_ID);
                add("quantity", CART_ITEM2_QUANTITY);
                add("unitPrice", CART_ITEM2_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM_RESPONSE3,
            new Rule() {
              {
                add("id", CART_ITEM3_ID);
                add("productId", CART_ITEM3_PRODUCT_ID);
                add("quantity", CART_ITEM3_QUANTITY);
                add("unitPrice", CART_ITEM3_UNIT_PRICE);
              }
            });
  }
}
