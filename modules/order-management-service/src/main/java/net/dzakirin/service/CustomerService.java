package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.constant.ErrorCodes;
import net.dzakirin.dto.request.CustomerUpsertRequest;
import net.dzakirin.dto.response.BaseListResponse;
import net.dzakirin.dto.response.BaseResponse;
import net.dzakirin.dto.response.CustomerResponse;
import net.dzakirin.exception.ResourceNotFoundException;
import net.dzakirin.exception.ValidationException;
import net.dzakirin.mapper.CustomerMapper;
import net.dzakirin.model.Customer;
import net.dzakirin.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public BaseResponse<CustomerResponse> createCustomer(CustomerUpsertRequest customerUpsertRequest) {
        CustomerUpsertRequestValidation(customerUpsertRequest);

        Customer customer = CustomerMapper.toCustomer(customerUpsertRequest);
        customerRepository.save(customer);

        return BaseResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer created successfully")
                .data(CustomerMapper.toCustomerResponse(customer))
                .build();
    }
    
    @Transactional
    public BaseResponse<CustomerResponse> updateCustomer(UUID userId, CustomerUpsertRequest updatedProductRequest) {
        CustomerUpsertRequestValidation(updatedProductRequest);

        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.CUSTOMER_NOT_FOUND.getMessage(userId.toString())));

        customer.setFirstName(updatedProductRequest.getFirstName());
        customer.setLastName(updatedProductRequest.getLastName());
        customer.setEmail(updatedProductRequest.getEmail());
        customerRepository.save(customer);

        return BaseResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer data updated successfully")
                .data(CustomerMapper.toCustomerResponse(customer))
                .build();
    }

    private void CustomerUpsertRequestValidation(CustomerUpsertRequest customerUpsertRequest) {
        if (customerUpsertRequest.getEmail() == null || customerUpsertRequest.getEmail().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_EMAIL_EMPTY.getMessage());
        }
        if (customerRepository.existsByEmail(customerUpsertRequest.getEmail())) {
            throw new ValidationException(CUSTOMER_EMAIL_ALREADY_EXISTS.getMessage());
        }
        if (!customerUpsertRequest.getEmail().matches(emailRegex)) {
            throw new ValidationException(CUSTOMER_EMAIL_INVALID.getMessage());
        }
        if (customerUpsertRequest.getFirstName() == null || customerUpsertRequest.getFirstName().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_FIRST_NAME_EMPTY.getMessage());
        }
        if (customerUpsertRequest.getFirstName().length() < 2 || customerUpsertRequest.getFirstName().length() > 100) {
            throw new ValidationException(CUSTOMER_FIRST_NAME_LENGTH.getMessage());
        }
        if (customerUpsertRequest.getLastName() == null || customerUpsertRequest.getLastName().trim().isEmpty()) {
            throw new ValidationException(CUSTOMER_LAST_NAME_EMPTY.getMessage());
        }
        if (customerUpsertRequest.getLastName().length() < 2 || customerUpsertRequest.getLastName().length() > 100) {
            throw new ValidationException(CUSTOMER_LAST_NAME_LENGTH.getMessage());
        }
    }
}
