package br.com.cams7.orders.adapter.configuration;

import br.com.cams7.orders.core.CreateOrderUseCase;
import br.com.cams7.orders.core.DeleteOrderByIdUseCase;
import br.com.cams7.orders.core.GetOrderByIdUseCase;
import br.com.cams7.orders.core.GetOrdersByCountryUseCase;
import br.com.cams7.orders.core.port.in.CreateOrderUseCasePort;
import br.com.cams7.orders.core.port.in.DeleteOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.in.GetOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.in.GetOrdersByCountryUseCasePort;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import br.com.cams7.orders.core.utils.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

  @Autowired private ZonePropertiesWithValues zoneProperties;

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public RestTemplate restTesmplate() {
    return new RestTemplate();
  }

  @Bean
  DateUtils dateUtils() {
    return new DateUtils(zoneProperties);
  }

  @Bean
  GetOrdersByCountryUseCasePort getOrdersUseCase(
      GetOrdersByCountryRepositoryPort getOrdersRepository) {
    return new GetOrdersByCountryUseCase(getOrdersRepository);
  }

  @Bean
  GetOrderByIdUseCasePort getOrderByIdUseCase(GetOrderByIdRepositoryPort getOrderByIdRepository) {
    return new GetOrderByIdUseCase(getOrderByIdRepository);
  }

  @Bean
  DeleteOrderByIdUseCasePort deleteOrderByIdUseCase(
      DeleteOrderByIdRepositoryPort deleteOrderByIdRepository) {
    return new DeleteOrderByIdUseCase(deleteOrderByIdRepository);
  }

  @Bean
  CreateOrderUseCasePort createOrderUseCase(
      GetCustomerServicePort getCustomerService,
      GetCustomerAddressServicePort getCustomerAddressService,
      GetCustomerCardServicePort getCustomerCardService,
      GetCartItemsServicePort getCartItemsService,
      VerifyPaymentServicePort verifyPaymentService,
      AddShippingOrderServicePort addShippingOrderService,
      CreateOrderRepositoryPort createOrderRepository,
      UpdateShippingByIdRepositoryPort updateShippingByIdRepository) {
    return new CreateOrderUseCase(
        getCustomerService,
        getCustomerAddressService,
        getCustomerCardService,
        getCartItemsService,
        verifyPaymentService,
        addShippingOrderService,
        createOrderRepository,
        updateShippingByIdRepository);
  }
}
