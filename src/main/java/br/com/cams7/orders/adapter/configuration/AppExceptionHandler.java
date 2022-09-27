package br.com.cams7.orders.adapter.configuration;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.getStatusText;
import static br.com.cams7.orders.core.utils.DateUtils.getFormattedDateTime;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import br.com.cams7.orders.core.utils.DateUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UrlPathHelper;

@ControllerAdvice
public class AppExceptionHandler {

  protected static final String PATH_ATTRIBUTE = "path";
  private static final String TIMESTAMP_ATTRIBUTE = "timestamp";
  private static final String MESSAGE_ATTRIBUTE = "message";
  private static final String EXCEPTION_ATTRIBUTE = "exception";
  private static final String STATUS_ATTRIBUTE = "status";
  private static final String ERROR_ATTRIBUTE = "error";
  private static final String REQUEST_ID_ATTRIBUTE = "requestId";

  private static final String ERRORS_ATTRIBUTE = "errors";
  private static final String ERROR_MESSAGE_SEPARATOR = ",";
  private static final String ATTRIBUTE_SEPARATOR = ":";

  @Autowired private DateUtils dateUtils;

  @ExceptionHandler(value = ResponseStatusException.class)
  protected ResponseEntity<Object> handleResponseStatusException(
      ResponseStatusException exception, WebRequest request, HttpServletRequest httpRequest) {
    return handleException(
        exception,
        getResponseStatusException(exception, httpRequest),
        HttpStatus.valueOf(exception.getStatusCode()),
        request);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException exception, WebRequest request, HttpServletRequest httpRequest) {
    return handleException(
        exception, getConstraintViolationException(exception, httpRequest), BAD_REQUEST, request);
  }

  @ExceptionHandler(value = HttpClientErrorException.class)
  protected ResponseEntity<Object> handleHttpClientErrorException(
      HttpClientErrorException exception, WebRequest request, HttpServletRequest httpRequest) {
    return handleException(
        exception,
        getHttpClientErrorException(exception, httpRequest),
        exception.getStatusCode(),
        request);
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleOtherException(
      RuntimeException exception, WebRequest request, HttpServletRequest httpRequest) {
    return handleException(
        exception, getOtherException(exception, httpRequest), INTERNAL_SERVER_ERROR, request);
  }

  private ResponseEntity<Object> handleException(
      Exception exception,
      Map<String, Object> errorAttributes,
      HttpStatus status,
      WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(APPLICATION_JSON);

    return new ResponseEntity<>(errorAttributes, headers, status);
  }

  private Map<String, Object> getResponseStatusException(
      ResponseStatusException exception, HttpServletRequest request) {
    var errorAttributes = new HashMap<String, Object>();
    switch (exception.getStatusCode()) {
      case BAD_REQUEST_CODE:
      case NOT_FOUND_CODE:
      case INTERNAL_SERVER_ERROR_CODE:
        setErrorAttributes(request, errorAttributes, exception);
        break;
      default:
        break;
    }
    setErrorAttributes(errorAttributes, exception.getMessage(), exception.getClass());
    return errorAttributes;
  }

  private Map<String, Object> getHttpClientErrorException(
      HttpClientErrorException exception, HttpServletRequest request) {
    var errorAttributes = new HashMap<String, Object>();
    setErrorAttributes(
        request, errorAttributes, exception.getStatusCode().value(), exception.getStatusText());
    setErrorAttributes(errorAttributes, exception.getMessage(), exception.getClass());
    return errorAttributes;
  }

  private Map<String, Object> getConstraintViolationException(
      ConstraintViolationException exception, HttpServletRequest request) {
    var errorAttributes = new HashMap<String, Object>();
    setErrorAttributes(request, errorAttributes, BAD_REQUEST_CODE, getStatusText(BAD_REQUEST_CODE));
    setErrorAttributes(errorAttributes, null, exception.getClass());
    errorAttributes.put(ERRORS_ATTRIBUTE, getValidationErrors(exception.getMessage()));
    return errorAttributes;
  }

  private Map<String, Object> getOtherException(
      RuntimeException exception, HttpServletRequest request) {
    var errorAttributes = new HashMap<String, Object>();
    setErrorAttributes(
        request,
        errorAttributes,
        INTERNAL_SERVER_ERROR_CODE,
        getStatusText(INTERNAL_SERVER_ERROR_CODE));
    setErrorAttributes(errorAttributes, exception.getMessage(), exception.getClass());
    return errorAttributes;
  }

  private void setErrorAttributes(
      HttpServletRequest request,
      Map<String, Object> errorAttributes,
      ResponseStatusException exception) {
    setErrorAttributes(
        request, errorAttributes, exception.getStatusCode(), exception.getStatusText());
  }

  private void setErrorAttributes(
      HttpServletRequest request,
      Map<String, Object> errorAttributes,
      int statusCode,
      String statusText) {
    errorAttributes.put(STATUS_ATTRIBUTE, statusCode);
    errorAttributes.put(ERROR_ATTRIBUTE, statusText);
    errorAttributes.put(REQUEST_ID_ATTRIBUTE, request.getHeader(REQUEST_TRACE_ID_HEADER));
    errorAttributes.put(
        TIMESTAMP_ATTRIBUTE,
        getFormattedDateTime(dateUtils.getZonedDateTime(request.getHeader(COUNTRY_HEADER))));
    errorAttributes.put(PATH_ATTRIBUTE, new UrlPathHelper().getPathWithinApplication(request));
  }

  private void setErrorAttributes(
      Map<String, Object> errorAttributes, String errorMessage, Class<?> exceptionType) {
    if (errorMessage != null) errorAttributes.put(MESSAGE_ATTRIBUTE, errorMessage);
    errorAttributes.put(EXCEPTION_ATTRIBUTE, exceptionType.getName());
  }

  private static List<ValidationError> getValidationErrors(String errorMessage) {
    return List.of(errorMessage.split(ERROR_MESSAGE_SEPARATOR)).stream()
        .map(AppExceptionHandler::getValidationError)
        .collect(Collectors.toList());
  }

  private static ValidationError getValidationError(String errorMessage) {
    var atributeWithMessage = errorMessage.split(ATTRIBUTE_SEPARATOR);
    var error = new ValidationError();
    error.setField(atributeWithMessage[0].trim());
    error.setMessage(atributeWithMessage[1].trim());
    return error;
  }

  @Data
  private static class ValidationError {
    private String message;
    private String field;
  }
}
