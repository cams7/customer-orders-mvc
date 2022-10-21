package br.com.cams7.orders.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  private String customerId;

  private String firstName;

  private String lastName;

  private String fullName;

  private String username;
}
