package br.com.cams7.orders.core.port.in.params;

import static br.com.cams7.orders.template.CreateOrderRequestTemplate.CART_ID;
import static br.com.cams7.orders.template.CreateOrderRequestTemplate.INVALID_VALUE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_POSTCODE;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_LONG_NUM;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateOrderCommandTests {

  @Test
  @DisplayName("Should create command instance when pass valid params")
  void shouldCreateCommandInstanceWhenPassValidParams() {
    new CreateOrderCommand(CUSTOMER_ID, CUSTOMER_ADDRESS_POSTCODE, CUSTOMER_CARD_LONG_NUM, CART_ID);
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass null params")
  void shouldNotCreateCommandInstanceWhenPassNullParams() {
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(null, null, null, null);
            });
    assertThat(exception.getMessage()).containsAnyOf("customerId: Customer id must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("addressPostcode: Address postcode must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cardNumber: Card number must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cartId: Cart id must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass empty params")
  void shouldNotCreateCommandInstanceWhenPassEmptyParams() {
    var emptyString = "";
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(emptyString, emptyString, emptyString, emptyString);
            });
    assertThat(exception.getMessage()).containsAnyOf("customerId: Customer id must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("addressPostcode: Address postcode must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cardNumber: Card number must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cartId: Cart id must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass blank params")
  void shouldNotCreateCommandInstanceWhenPassBlankParams() {
    var blankString = " ";
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(blankString, blankString, blankString, blankString);
            });
    assertThat(exception.getMessage()).containsAnyOf("customerId: Customer id must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("addressPostcode: Address postcode must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cardNumber: Card number must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("cartId: Cart id must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass invalid URLs")
  void shouldNotCreateCommandInstanceWhenPassInvalidUrls() {
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(INVALID_VALUE, INVALID_VALUE, INVALID_VALUE, INVALID_VALUE);
            });
    assertThat(exception.getMessage()).containsAnyOf("customerId: Invalid customer id");
    assertThat(exception.getMessage()).containsAnyOf("addressPostcode: Invalid address postcode");
    assertThat(exception.getMessage()).containsAnyOf("cardNumber: Invalid card number");
    assertThat(exception.getMessage()).containsAnyOf("cartId: Invalid cart id");
  }
}
