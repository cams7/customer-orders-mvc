package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_DESCRIPTION;
import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_EXAMPLE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_DESCRIPTION;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_EXAMPLE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_200_CODE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_200_OK;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_201_CODE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_201_CREATED;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_400_BAD_REQUEST;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_400_CODE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_404_CODE;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_404_NOT_FOUND;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_500_BAD_INTERNAL_SERVER_ERROR;
import static br.com.cams7.orders.adapter.commons.ApiConstants.STATUS_500_CODE;
import static br.com.cams7.orders.core.utils.DateUtils.getFormattedDateTime;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.CreateOrderUseCasePort;
import br.com.cams7.orders.core.port.in.DeleteOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.in.GetOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.in.GetOrdersByCountryUseCasePort;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer orders")
@RequiredArgsConstructor
// @Log4j2
@RestController
@RequestMapping(path = "/orders", produces = APPLICATION_JSON_VALUE)
public class OrderController {

  private final GetOrdersByCountryUseCasePort getOrdersByCountryUseCase;
  private final GetOrderByIdUseCasePort getOrderByIdUseCase;
  private final DeleteOrderByIdUseCasePort deleteOrderByIdUseCase;
  private final CreateOrderUseCasePort createOrderUseCase;
  private final ModelMapper modelMapper;

  @Operation(description = "Get orders")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping
  List<OrderResponse> getOrders(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId) {
    return getOrdersByCountryUseCase.execute(country).stream()
        .map(this::getOrder)
        .collect(Collectors.toList());
  }

  @Operation(description = "Get order")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping("/{orderId}")
  OrderResponse getOrder(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId,
      @Parameter(
              name = "orderId",
              required = true,
              description = "Order id",
              example = "57a98d98e4b00679b4a830af")
          @PathVariable
          String orderId) {
    return getOrder(getOrderByIdUseCase.execute(country, orderId));
  }

  @Operation(description = "Delete order")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @DeleteMapping("/{orderId}")
  void deleteOrder(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId,
      @Parameter(
              name = "orderId",
              required = true,
              description = "Order id",
              example = "57a98d98e4b00679b4a830af")
          @PathVariable
          String orderId) {
    deleteOrderByIdUseCase.execute(country, orderId);
  }

  @Operation(description = "Create order")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_201_CODE, description = STATUS_201_CREATED),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_404_CODE,
        description = STATUS_404_NOT_FOUND,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  OrderResponse createOrder(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId,
      @RequestBody CreateOrderRequest request) {
    return getOrder(createOrderUseCase.execute(country, getCreateOrder(request)));
  }

  private OrderResponse getOrder(OrderEntity entity) {
    if (entity == null) return null;
    var response = modelMapper.map(entity, OrderResponse.class);
    response.setRegistrationDate(getFormattedDateTime(entity.getRegistrationDate()));
    return response;
  }

  private CreateOrderCommand getCreateOrder(CreateOrderRequest request) {
    return new CreateOrderCommand(
        request.getCustomerUrl(),
        request.getAddressUrl(),
        request.getCardUrl(),
        request.getItemsUrl());
  }
}
