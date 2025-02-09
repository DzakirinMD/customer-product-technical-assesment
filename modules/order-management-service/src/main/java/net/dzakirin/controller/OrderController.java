package net.dzakirin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dzakirin.dto.request.OrderRequest;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.dto.response.OrderResponse;
import net.dzakirin.service.OrderService;
import net.dzakirin.utils.PaginationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders with pagination")
    @GetMapping
    public ResponseEntity<BaseListResponse<OrderResponse>> getAllOrders(
            @Parameter(description = "Page number (starts from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size", example = "5")
            @RequestParam(defaultValue = "5") int size,

            @Parameter(description = "Sorting field",
                    array = @ArraySchema(schema = @Schema(allowableValues = {"orderDate", "customerId"},
                            type = "string")))
            @RequestParam(defaultValue = "orderDate") String[] sort) {
        Pageable pageable = PaginationUtils.getPageRequest(page, size, sort);
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }


    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponse>> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }
}
