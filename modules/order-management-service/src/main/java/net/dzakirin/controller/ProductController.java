package net.dzakirin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.dto.response.BaseListResponse;
import net.dzakirin.dto.response.BaseResponse;
import net.dzakirin.dto.response.ProductResponse;
import net.dzakirin.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products with pagination")
    @GetMapping
    public ResponseEntity<BaseListResponse<ProductResponse>> getAllProducts(Pageable pageable) {
        Page<ProductResponse> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(new BaseListResponse<>(true, "Products fetched successfully",
                products.getContent(), products.getTotalElements(), products.getTotalPages()));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(new BaseResponse<>(true, "Product found", product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<BaseResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.ok(new BaseResponse<>(true, "Product created successfully", productResponse));
    }

    @Operation(summary = "Update an existing product")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> updateProduct(@PathVariable UUID id,
                                                                       @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(new BaseResponse<>(true, "Product updated successfully", updatedProduct));
    }

    @Operation(summary = "Delete product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
