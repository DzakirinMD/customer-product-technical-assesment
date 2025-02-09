package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.dto.request.CustomerCreationRequest;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.dto.response.CustomerResponse;
import net.dzakirin.model.Customer;
import net.dzakirin.model.Product;

import java.util.List;

@UtilityClass
public class CustomerMapper {

    public static CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();
    }

    public static List<CustomerResponse> toResponseList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toCustomerResponse)
                .toList();
    }

    public static Customer toCustomer(CustomerCreationRequest customerCreationRequest) {
        if (customerCreationRequest == null) {
            return null;
        }

        return Customer.builder()
                .firstName(customerCreationRequest.getFirstName())
                .lastName(customerCreationRequest.getLastName())
                .email(customerCreationRequest.getEmail())
                .build();
    }
}
