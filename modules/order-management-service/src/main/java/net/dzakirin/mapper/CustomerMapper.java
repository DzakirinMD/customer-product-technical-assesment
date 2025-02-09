package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.dto.request.CustomerUpsertRequest;
import net.dzakirin.dto.response.CustomerResponse;
import net.dzakirin.model.Customer;

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

    public static Customer toCustomer(CustomerUpsertRequest customerUpsertRequest) {
        if (customerUpsertRequest == null) {
            return null;
        }

        return Customer.builder()
                .firstName(customerUpsertRequest.getFirstName())
                .lastName(customerUpsertRequest.getLastName())
                .email(customerUpsertRequest.getEmail())
                .build();
    }
}
