package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.dto.request.CustomerCreationRequest;
import net.dzakirin.dto.response.BaseListResponse;
import net.dzakirin.dto.response.BaseResponse;
import net.dzakirin.dto.response.CustomerResponse;
import net.dzakirin.exception.ValidationException;
import net.dzakirin.mapper.CustomerMapper;
import net.dzakirin.model.Customer;
import net.dzakirin.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static net.dzakirin.constant.ErrorCodes.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private String emailRegex = "^[A-Za-z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    private final CustomerRepository customerRepository;

    public BaseListResponse<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);

        return BaseListResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customers fetched successfully")
                .data(CustomerMapper.toResponseList(customers.getContent()))
                .totalRecords(customers.getTotalElements())
                .totalPages(customers.getTotalPages())
                .build();
    }

    @Transactional
    public BaseResponse<CustomerResponse> createCustomer(CustomerCreationRequest customerCreationRequest) {
        customerRequestCreationValidation(customerCreationRequest);

        Customer customer = CustomerMapper.toCustomer(customerCreationRequest);
        customerRepository.save(customer);

        return BaseResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer created successfully")
                .data(CustomerMapper.toCustomerResponse(customer))
                .build();
    }

    private void customerRequestCreationValidation(CustomerCreationRequest customerCreationRequest) {
        if (customerCreationRequest.getEmail() == null || customerCreationRequest.getEmail().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_EMAIL_EMPTY.getMessage());
        }
        if (customerRepository.existsByEmail(customerCreationRequest.getEmail())) {
            throw new ValidationException(CUSTOMER_EMAIL_ALREADY_EXISTS.getMessage());
        }
        if (!customerCreationRequest.getEmail().matches(emailRegex)) {
            throw new ValidationException(CUSTOMER_EMAIL_INVALID.getMessage());
        }
        if (customerCreationRequest.getFirstName() == null || customerCreationRequest.getFirstName().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_FIRST_NAME_EMPTY.getMessage());
        }
        if (customerCreationRequest.getFirstName().length() < 2 || customerCreationRequest.getFirstName().length() > 100) {
            throw new ValidationException(CUSTOMER_FIRST_NAME_LENGTH.getMessage());
        }
        if (customerCreationRequest.getLastName() == null || customerCreationRequest.getLastName().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_LAST_NAME_EMPTY.getMessage());
        }
        if (customerCreationRequest.getLastName().length() < 2 || customerCreationRequest.getLastName().length() > 100) {
            throw new ValidationException(CUSTOMER_LAST_NAME_LENGTH.getMessage());
        }
    }
}
