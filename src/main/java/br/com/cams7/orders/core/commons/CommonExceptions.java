package br.com.cams7.orders.core.commons;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_CODE;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class CommonExceptions {

  public static ResponseStatusException responseNotFoundException(String message) {
    return new ResponseStatusException(message, NOT_FOUND_CODE);
  }

  public static ResponseStatusException responseInternalServerErrorException(String message) {
    return new ResponseStatusException(message, INTERNAL_SERVER_ERROR_CODE);
  }
}
