package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.INVALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_MODEL;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.SHIPPING_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
public class OrderControllerTests extends BaseIntegrationTests {

  private static final String INVALID_COUNTRY = "DO";
  private static final String PAYMENT_URL = "http://test/payments";
  private static final String SHIPPING_URL = "http://test/shippings";

  private static final String PATH = "/orders";

  private static String ERRORS0_MESSAGE_ATTRIBUTE = "$.errors[0].message";
  private static String ERRORS0_FIELD_ATTRIBUTE = "$.errors[0].field";

  @AfterEach
  void dropCollection() {
    dropCollection(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME);
  }

  @Test
  @DisplayName("Should return orders when accessing 'get orders' API and pass a valid country")
  void shouldReturnOrdersWhenAccessingGetOrdersAPIAndPassAValidCountry() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    var resultActions =
        mockMvc
            .perform(
                get(PATH)
                    .header("country", CUSTOMER_ADDRESS_COUNTRY)
                    .header("requestTraceId", REQUEST_TRACE_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    var data = getResponse(resultActions, OrderResponse[].class);

    assertThat(data).isEqualTo(new OrderResponse[] {response});
  }

  @Test
  @DisplayName(
      "Should return empty list when accessing 'get orders' API and doesn't have any orders")
  void shouldReturnEmptyListWhenAccessingGetOrdersAPIAndDoesNotHaveAnyOrders() throws Exception {
    var resultActions =
        mockMvc
            .perform(
                get(PATH)
                    .header("country", CUSTOMER_ADDRESS_COUNTRY)
                    .header("requestTraceId", REQUEST_TRACE_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    var data = getResponse(resultActions, OrderResponse[].class);

    assertThat(data.length).isEqualTo(0);
  }

  @Test
  @DisplayName(
      "Should return empty list when accessing 'get orders' API and when pass a invalid country")
  void shouldReturnEmptyListWhenAccessingGetOrdersAPIAndPassAInvalidCountry() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    var resultActions =
        mockMvc
            .perform(
                get(PATH)
                    .header("country", INVALID_COUNTRY)
                    .header("requestTraceId", REQUEST_TRACE_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    var data = getResponse(resultActions, OrderResponse[].class);

    assertThat(data.length).isEqualTo(0);
  }

  @Test
  @DisplayName("Should return order when accessing 'get order' API and pass a valid order id")
  void shouldReturnOrderWhenAccessingGetOrderAPIAndPassAValidOrderId() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    var resultActions =
        mockMvc
            .perform(
                get(String.format("%s/{orderId}", PATH), model.getId())
                    .header("country", CUSTOMER_ADDRESS_COUNTRY)
                    .header("requestTraceId", REQUEST_TRACE_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    var data = getResponse(resultActions, OrderResponse.class);

    assertThat(data).isEqualTo(response);
  }

  @Test
  @DisplayName("Should return empty when accessing 'get order' API and doesn't have any order")
  void shouldReturnEmptyWhenAccessingGetOrderAPIAndDoesNotHaveOrder() throws Exception {
    mockMvc
        .perform(
            get(String.format("%s/{orderId}", PATH), ORDER_ID)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  @DisplayName("Should return empty when accessing 'get order' API and when pass a invalid country")
  void shouldReturnEmptyWhenAccessingGetOrderAPIAndPassAInvalidCountry() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    mockMvc
        .perform(
            get(String.format("%s/{orderId}", PATH), model.getId())
                .header("country", INVALID_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  @DisplayName("Should delete order when accessing 'delete order' API and pass a valid order id")
  void shouldDeleteOrderWhenAccessingDeleteOrderAPIAndPassAValidOrderId() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    mockMvc
        .perform(
            delete(String.format("%s/{orderId}", PATH), model.getId())
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").is(model.getId())),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));
    assertThat(total).isEqualTo(0l);
  }

  @Test
  @DisplayName("Should do nothing when accessing 'delete order' API and doesn't have any order")
  void shouldDoNothingWhenAccessingDeleteOrderAPIAndDoesNotHaveOrder() throws Exception {
    mockMvc
        .perform(
            delete(String.format("%s/{orderId}", PATH), ORDER_ID)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  @DisplayName("Should do nothing when accessing 'delete order' API and pass a invalid country")
  void shouldDoNothingWhenAccessingDeleteOrderAPIAndPassAInvalidCountry() throws Exception {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    mockMvc
        .perform(
            delete(String.format("%s/{orderId}", PATH), model.getId())
                .header("country", INVALID_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").is(model.getId())),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));

    assertThat(total).isEqualTo(1l);
  }

  @Test
  @DisplayName("Should return created order when accessing 'create order' API and pass valid URLs")
  void shouldReturnCreatedOrderWhenAccessingCreateOrderAPIAndPassValidURLs() throws Exception {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

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
    ShippingResponse shippingResponse = from(ShippingResponse.class).gimme(SHIPPING_RESPONSE);

    mockGet(request.getCustomerUrl(), customerResponse);
    mockGet(request.getAddressUrl(), customerAddressResponse);
    mockGet(request.getCardUrl(), customerCardResponse);
    mockGet(
        request.getItemsUrl(),
        cartItemsResponse,
        new ParameterizedTypeReference<List<CartItemResponse>>() {});
    mockPost(PAYMENT_URL, paymentResponse);
    mockPost(SHIPPING_URL, shippingResponse);

    mockMvc
        .perform(
            post(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.orderId", not(emptyString())))
        .andExpect(jsonPath("$.customer.customerId", is(response.getCustomer().getCustomerId())))
        .andExpect(jsonPath("$.customer.fullName", is(response.getCustomer().getFullName())))
        .andExpect(jsonPath("$.customer.username", is(response.getCustomer().getUsername())))
        .andExpect(jsonPath("$.address.addressId", is(response.getAddress().getAddressId())))
        .andExpect(jsonPath("$.address.number", is(response.getAddress().getNumber())))
        .andExpect(jsonPath("$.address.street", is(response.getAddress().getStreet())))
        .andExpect(jsonPath("$.address.postcode", is(response.getAddress().getPostcode())))
        .andExpect(jsonPath("$.address.city", is(response.getAddress().getCity())))
        .andExpect(
            jsonPath("$.address.federativeUnit", is(response.getAddress().getFederativeUnit())))
        .andExpect(jsonPath("$.address.country", is(response.getAddress().getCountry())))
        .andExpect(jsonPath("$.card.cardId", is(response.getCard().getCardId())))
        .andExpect(jsonPath("$.items[0].productId", is(response.getItems().get(0).getProductId())))
        .andExpect(jsonPath("$.items[0].quantity", is(response.getItems().get(0).getQuantity())))
        //    .andExpect(jsonPath("$.items[0].unitPrice",
        // is(response.getItems().get(0).getUnitPrice())))
        .andExpect(jsonPath("$.items[1].productId", is(response.getItems().get(1).getProductId())))
        .andExpect(jsonPath("$.items[1].quantity", is(response.getItems().get(1).getQuantity())))
        //    .andExpect(jsonPath("$.items[1].unitPrice",
        // is(response.getItems().get(1).getUnitPrice())))
        .andExpect(jsonPath("$.registrationDate", not(emptyString())))
    //    .andExpect(jsonPath("$.totalAmount", is(response.getTotalAmount())))
    ;

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").exists(true)),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));

    assertThat(total).isEqualTo(1l);
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and decline payment")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndDeclinePayment() throws Exception {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse =
        List.of(
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE1),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE2),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE3));
    PaymentResponse paymentResponse = from(PaymentResponse.class).gimme(DECLINED_PAYMENT_RESPONSE);

    mockGet(request.getCustomerUrl(), customerResponse);
    mockGet(request.getAddressUrl(), customerAddressResponse);
    mockGet(request.getCardUrl(), customerCardResponse);
    mockGet(
        request.getItemsUrl(),
        cartItemsResponse,
        new ParameterizedTypeReference<List<CartItemResponse>>() {});
    mockPost(PAYMENT_URL, paymentResponse);

    mockMvc
        .perform(
            post(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(PATH)))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(BAD_REQUEST_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(BAD_REQUEST_NAME)))
        .andExpect(jsonPath(MESSAGE_ATTRIBUTE, is(paymentResponse.getMessage())))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)))
        .andExpect(jsonPath(EXCEPTION_ATTRIBUTE, is(ResponseStatusException.class.getName())));

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").exists(true)),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));

    assertThat(total).isEqualTo(0l);
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and don't have cart items")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndDoNotHaveCartItems()
      throws Exception {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse = List.of();

    mockGet(request.getCustomerUrl(), customerResponse);
    mockGet(request.getAddressUrl(), customerAddressResponse);
    mockGet(request.getCardUrl(), customerCardResponse);
    mockGet(
        request.getItemsUrl(),
        cartItemsResponse,
        new ParameterizedTypeReference<List<CartItemResponse>>() {});

    mockMvc
        .perform(
            post(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(PATH)))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(BAD_REQUEST_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(BAD_REQUEST_NAME)))
        .andExpect(jsonPath(MESSAGE_ATTRIBUTE, is("There aren't items in the cart")))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)))
        .andExpect(jsonPath(EXCEPTION_ATTRIBUTE, is(ResponseStatusException.class.getName())));

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").exists(true)),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));

    assertThat(total).isEqualTo(0l);
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and pass some invalid URL")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndPassSomeInvalidURL()
      throws Exception {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(INVALID_CREATE_ORDER_REQUEST);

    mockMvc
        .perform(
            post(PATH)
                .header("country", CUSTOMER_ADDRESS_COUNTRY)
                .header("requestTraceId", REQUEST_TRACE_ID)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath(TIMESTAMP_ATTRIBUTE, not(emptyString())))
        .andExpect(jsonPath(PATH_ATTRIBUTE, is(PATH)))
        .andExpect(jsonPath(STATUS_ATTRIBUTE, is(BAD_REQUEST_CODE)))
        .andExpect(jsonPath(ERROR_ATTRIBUTE, is(BAD_REQUEST_NAME)))
        .andExpect(jsonPath(REQUESTID_ATTRIBUTE, is(REQUEST_TRACE_ID)))
        .andExpect(jsonPath(EXCEPTION_ATTRIBUTE, is(ConstraintViolationException.class.getName())))
        .andExpect(jsonPath(ERRORS0_MESSAGE_ATTRIBUTE, is("Invalid customer url")))
        .andExpect(jsonPath(ERRORS0_FIELD_ATTRIBUTE, is("customerUrl")));

    var total =
        mongoTemplate.count(
            new Query().addCriteria(where("id").exists(true)),
            OrderModel.class,
            getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME));

    assertThat(total).isEqualTo(0l);
  }

  private void createOrderCollection(String country, OrderModel order) {
    var collectionName = getCollectionName(country, COLLECTION_NAME);
    mongoTemplate.insert(order, collectionName);
  }
}
