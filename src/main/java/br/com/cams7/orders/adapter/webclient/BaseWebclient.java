package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.post;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;

public abstract class BaseWebclient {

  protected static String getCountry() {
    return MDC.get(COUNTRY_HEADER);
  }

  protected static String getRequestTraceId() {
    return MDC.get(REQUEST_TRACE_ID_HEADER);
  }

  protected static RequestEntity<Void> getRequest(String url) {
    return getRequest(url, getHeaders());
  }

  private static RequestEntity<Void> getRequest(String url, HttpHeaders headers) {
    return get(url).accept(APPLICATION_JSON).headers(headers).build();
  }

  protected static <T> RequestEntity<T> getRequest(String url, T body) {
    return getRequest(url, getHeaders(), body);
  }

  private static <T> RequestEntity<T> getRequest(String url, HttpHeaders headers, T body) {
    return post(url)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .headers(headers)
        .body(body);
  }

  private static HttpHeaders getHeaders() {
    var headers = new HttpHeaders();
    headers.add(COUNTRY_HEADER, getCountry());
    headers.add(REQUEST_TRACE_ID_HEADER, getRequestTraceId());
    return headers;
  }
}
