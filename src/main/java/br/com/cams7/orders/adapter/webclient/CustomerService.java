package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService extends BaseWebclient
    implements GetCustomerServicePort, GetCustomerAddressServicePort, GetCustomerCardServicePort {

  private final RestTemplate restTemplate;
  private final ModelMapper modelMapper;

  @Override
  public Future<Customer> getCustomer(String country, String requestTraceId, String customerUrl) {
    var customer =
        getCustomer(
            restTemplate
                .exchange(getRequest(customerUrl, country, requestTraceId), CustomerResponse.class)
                .getBody());
    return new AsyncResult<>(customer);
  }

  @Override
  public Future<CustomerAddress> getCustomerAddress(
      String country, String requestTraceId, String addressUrl) {
    var customerAddress =
        getCustomerAddress(
            restTemplate
                .exchange(
                    getRequest(addressUrl, country, requestTraceId), CustomerAddressResponse.class)
                .getBody());
    return new AsyncResult<>(customerAddress);
  }

  @Override
  public Future<CustomerCard> getCustomerCard(
      String country, String requestTraceId, String cardUrl) {
    var customerCard =
        getCustomerCard(
            restTemplate
                .exchange(getRequest(cardUrl, country, requestTraceId), CustomerCardResponse.class)
                .getBody());
    return new AsyncResult<>(customerCard);
  }

  private Customer getCustomer(CustomerResponse response) {
    if (response == null) return null;
    var customer = modelMapper.map(response, Customer.class);
    customer.setCustomerId(response.getId());
    return customer;
  }

  private CustomerAddress getCustomerAddress(CustomerAddressResponse response) {
    if (response == null) return null;
    var customerAddress = modelMapper.map(response, CustomerAddress.class);
    customerAddress.setAddressId(response.getId());
    return customerAddress;
  }

  private CustomerCard getCustomerCard(CustomerCardResponse response) {
    if (response == null) return null;
    var customerCard = modelMapper.map(response, CustomerCard.class);
    customerCard.setCardId(response.getId());
    return customerCard;
  }
}
