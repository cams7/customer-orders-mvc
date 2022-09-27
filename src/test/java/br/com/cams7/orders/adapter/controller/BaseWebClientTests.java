package br.com.cams7.orders.adapter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import br.com.cams7.orders.BaseTests;
import java.util.List;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseWebClientTests extends BaseTests {

  @MockBean private RestTemplate restTemplate;

  @SuppressWarnings("unchecked")
  protected <T> void mockGet(String url, T response) {
    var responseEntityMock = mock(ResponseEntity.class);
    given(restTemplate.exchange(any(RequestEntity.class), eq(response.getClass())))
        .willReturn(responseEntityMock);
    given(responseEntityMock.getBody()).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  protected <T> void mockGet(
      String url, List<T> response, ParameterizedTypeReference<List<T>> responseType) {
    var responseEntityMock = mock(ResponseEntity.class);
    given(restTemplate.exchange(any(RequestEntity.class), eq(responseType)))
        .willReturn(responseEntityMock);
    given(responseEntityMock.getBody()).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  protected <T> void mockPost(String url, T response) {
    var responseEntityMock = mock(ResponseEntity.class);
    given(restTemplate.exchange(any(RequestEntity.class), eq(response.getClass())))
        .willReturn(responseEntityMock);
    given(responseEntityMock.getBody()).willReturn(response);
  }
}
