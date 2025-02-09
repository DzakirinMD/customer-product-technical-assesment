package net.dzakirin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.dto.response.ProductResponse;
import net.dzakirin.service.ProductService;
import net.dzakirin.utils.PaginationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products with pagination")
    @GetMapping
    public ResponseEntity<BaseListResponse<ProductResponse>> getAllProducts(
            @Parameter(description = "Page number (starts from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size", example = "5")
            @RequestParam(defaultValue = "5") int size,

            @Parameter(description = "Sorting field",
                    array = @ArraySchema(schema = @Schema(allowableValues = {"title", "price", "stock"},
                            type = "string")))
            @RequestParam(defaultValue = "title") String[] sort) {
        Pageable pageable = PaginationUtils.getPageRequest(page, size, sort);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<BaseResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @Operation(summary = "Update an existing product (supports partial update)")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
