package br.com.cams7.orders.core.port.in.params;

import br.com.cams7.orders.core.commons.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class CreateOrderCommand extends SelfValidating<CreateOrderCommand> {

  private static final String BLANK_REGEX = "(^$)|(^\\s+$)";
  private static final String ID_REGEX = "(^[\\w\\-]+$)|" + BLANK_REGEX;
  private static final String POSTCODE_REGEX = "(^[\\w\\-]+$)|" + BLANK_REGEX;
  private static final String CARD_NUMBER_REGEX = "(^\\d+$)|" + BLANK_REGEX;

  @NotBlank(message = "Customer id must not be blank")
  @Pattern(regexp = ID_REGEX, message = "Invalid customer id")
  private final String customerId;

  @NotBlank(message = "Address postcode must not be blank")
  @Pattern(regexp = POSTCODE_REGEX, message = "Invalid address postcode")
  private final String addressPostcode;

  @NotBlank(message = "Card number must not be blank")
  @Pattern(regexp = CARD_NUMBER_REGEX, message = "Invalid card number")
  private final String cardNumber;

  @NotBlank(message = "Cart id must not be blank")
  @Pattern(regexp = ID_REGEX, message = "Invalid cart id")
  private final String cartId;

  public CreateOrderCommand(
      String customerId, String addressPostcode, String cardNumber, String cartId) {
    super();
    this.customerId = customerId;
    this.addressPostcode = addressPostcode;
    this.cardNumber = cardNumber;
    this.cartId = cartId;
    this.validateSelf();
  }
}
