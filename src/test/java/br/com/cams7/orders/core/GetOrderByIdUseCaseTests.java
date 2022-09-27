package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetOrderByIdUseCaseTests extends BaseTests {

  @InjectMocks private GetOrderByIdUseCase getOrderByIdUseCase;

  @Mock private GetOrderByIdRepositoryPort getOrderByIdRepository;

  @Test
  @DisplayName("Should get order when pass valid order id")
  void shouldGetOrderWhenPassValidOrderId() {
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);

    given(getOrderByIdRepository.getOrder(anyString(), anyString())).willReturn(order);

    var data = getOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID);

    assertThat(data).isEqualTo(order);

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName("Should throw error when 'get order by id in database' throws error")
  void shouldThrowErrorWhenGetOrderByIdInDatabaseThrowsError() {

    given(getOrderByIdRepository.getOrder(anyString(), anyString()))
        .willThrow(new RuntimeException(ERROR_MESSAGE));

    var exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              getOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID);
            });
    assertThat(exception.getMessage()).isEqualTo(ERROR_MESSAGE);

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }
}
