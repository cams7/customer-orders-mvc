package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_ORDER_ENTITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
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
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetOrdersByCountryUseCaseTests extends BaseTests {

  @InjectMocks private GetOrdersByCountryUseCase getOrdersByCountryUseCase;

  @Mock private GetOrdersByCountryRepositoryPort getOrdersByCountryRepository;

  @Test
  @DisplayName("Should get orders when pass valid country")
  void shouldGetOrdersWhenPassValidCountry() {
    List<OrderEntity> orders =
        List.of(
            from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY),
            from(OrderEntity.class).gimme(DECLINED_ORDER_ENTITY));

    given(getOrdersByCountryRepository.getOrders(anyString())).willReturn(orders);

    var data = getOrdersByCountryUseCase.execute(CUSTOMER_ADDRESS_COUNTRY);

    assertThat(data).isEqualTo(orders);

    then(getOrdersByCountryRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }

  @Test
  @DisplayName("Should throw error when 'get orders in database' throws error")
  void shouldThrowErrorWhenGetOrdersInDatabaseThrowsError() {

    given(getOrdersByCountryRepository.getOrders(anyString()))
        .willThrow(new RuntimeException(ERROR_MESSAGE));

    var exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              getOrdersByCountryUseCase.execute(CUSTOMER_ADDRESS_COUNTRY);
            });
    assertThat(exception.getMessage()).isEqualTo(ERROR_MESSAGE);

    then(getOrdersByCountryRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }
}
