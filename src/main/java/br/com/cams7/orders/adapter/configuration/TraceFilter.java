package br.com.cams7.orders.adapter.configuration;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.IOException;
import java.time.Instant;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Order(1)
public class TraceFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var start = Instant.now();
    var httpRequest = (HttpServletRequest) request;
    var requestTraceId = getRequestTraceId(httpRequest);
    var country = getCountry(httpRequest);

    createTrace(requestTraceId, country);

    chain.doFilter(httpRequest, response);

    var totalTime = MILLIS.between(start, Instant.now());
    log.info("The request spent {} millis to be completed", totalTime);
  }

  private static void createTrace(String requestTraceId, String country) {
    if (!isEmpty(requestTraceId)) MDC.put(REQUEST_TRACE_ID_HEADER, requestTraceId);
    if (!isEmpty(country)) MDC.put(COUNTRY_HEADER, country);
  }

  private static String getRequestTraceId(HttpServletRequest request) {
    return request.getHeader(REQUEST_TRACE_ID_HEADER);
  }

  private static String getCountry(HttpServletRequest request) {
    return request.getHeader(COUNTRY_HEADER);
  }
}
