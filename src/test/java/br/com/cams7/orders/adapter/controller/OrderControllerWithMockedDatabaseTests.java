package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import com.mongodb.MongoException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "300000")
public class OrderControllerWithMockedDatabaseTests extends BaseIntegrationTests {

  private static final String PAYMENT_URL = "http://test/payments";

  private static final String PATH = "/orders";

  @MockBean private GetOrdersByCountryRepositoryPort getOrdersRepository;
  @MockBean private GetOrderByIdRepositoryPort getOrderByIdRepository;
  @MockBean private DeleteOrderByIdRepositoryPort deleteOrderByIdRepository;
  @MockBean private CreateOrderRepositoryPort createOrderRepository;

  @Captor private ArgumentCaptor<OrderEntity> orderEntityCaptor;

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'get orders' API and some error happen trying to get orders in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingGetOrdersAPIAndSomeErrorHappenTryingToGetOrdersInDatabase()
          throws Exception {
    given(getOrdersRepository.getOrders(anyString())).willThrow(new MongoException(ERROR_MESSAGE));

    mockMvc
        .perform(
            get(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(PATH)))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_NAME)))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)));

    then(getOrdersRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'get order' API and some error happen trying to get order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingGetOrderAPIAndSomeErrorHappenTryingToGetOrderInDatabase()
          throws Exception {
    given(getOrderByIdRepository.getOrder(anyString(), anyString()))
        .willThrow(new MongoException(ERROR_MESSAGE));

    mockMvc
        .perform(
            get(String.format("%s/{orderId}", PATH), ORDER_ID)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(String.format("%s/%s", PATH, ORDER_ID))))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_NAME)))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)));

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'delete order' API and some error happen trying to delete order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingDeleteOrderAPIAndSomeErrorHappenTryingToDeleteOrderInDatabase()
          throws Exception {
    given(deleteOrderByIdRepository.delete(anyString(), anyString()))
        .willThrow(new MongoException(ERROR_MESSAGE));

    mockMvc
        .perform(
            delete(String.format("%s/{orderId}", PATH), ORDER_ID)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(String.format("%s/%s", PATH, ORDER_ID))))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_NAME)))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)));

    then(deleteOrderByIdRepository)
        .should(times(1))
        .delete(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'create order' API and some error happen trying to create order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingCreateOrderAPIAndSomeErrorHappenTryingToCreateOrderInDatabase()
          throws Exception {
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse =
        List.of(
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE1),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE3));
    PaymentResponse paymentResponse =
        from(PaymentResponse.class).gimme(AUTHORISED_PAYMENT_RESPONSE);

    mockGet(request.getCustomerUrl(), customerResponse);
    mockGet(request.getAddressUrl(), customerAddressResponse);
    mockGet(request.getCardUrl(), customerCardResponse);
    mockGet(
        request.getItemsUrl(),
        cartItemsResponse,
        new ParameterizedTypeReference<List<CartItemResponse>>() {});
    mockPost(PAYMENT_URL, paymentResponse);

    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willThrow(new MongoException(ERROR_MESSAGE));

    mockMvc
        .perform(
            post(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().is5xxServerError())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(PATH)))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(INTERNAL_SERVER_ERROR_NAME)))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)));

    then(createOrderRepository)
        .should(times(1))
        .create(eq(CUSTOMER_ADDRESS_COUNTRY), orderEntityCaptor.capture());
    var capturedOrder = orderEntityCaptor.getValue();

    assertThat(capturedOrder.getCustomer()).isEqualTo(order.getCustomer());
    assertThat(capturedOrder.getAddress()).isEqualTo(order.getAddress());
    assertThat(capturedOrder.getCard()).isEqualTo(order.getCard());
    assertThat(capturedOrder.getItems()).isEqualTo(order.getItems());
    assertThat(capturedOrder.getPayment()).isEqualTo(order.getPayment());
    assertThat(capturedOrder.getTotalAmount()).isEqualTo(order.getTotalAmount());
    assertThat(capturedOrder.getOrderId()).isNull();
    assertThat(capturedOrder.getRegistrationDate()).isNotNull();
  }
}
