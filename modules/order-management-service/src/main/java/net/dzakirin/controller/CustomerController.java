package net.dzakirin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dzakirin.dto.request.CustomerUpsertRequest;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.dto.response.CustomerResponse;
import net.dzakirin.service.CustomerService;
import net.dzakirin.utils.PaginationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    private ResponseEntity<BaseListResponse<CustomerResponse>> getAllCustomers(
            @Parameter(description = "Page number (starts from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size", example = "5")
            @RequestParam(defaultValue = "5") int size,

            @Parameter(description = "Sorting field",
                    array = @ArraySchema(schema = @Schema(allowableValues = {"firstName", "lastName", "email"},
                            type = "string")))
            @RequestParam(defaultValue = "email") String[] sort) {
        Pageable pageable = PaginationUtils.getPageRequest(page, size, sort);
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<BaseResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerUpsertRequest customerUpsertRequest) {
        return ResponseEntity.ok(customerService.createCustomer(customerUpsertRequest));
    }

    @Operation(summary = "Update an existing customer")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerUpsertRequest customerUpsertRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerUpsertRequest));
    }
}
