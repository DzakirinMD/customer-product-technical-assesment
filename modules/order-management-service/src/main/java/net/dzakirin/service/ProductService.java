package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.constant.ErrorCodes;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.dto.response.ProductResponse;
import net.dzakirin.exception.ResourceNotFoundException;
import net.dzakirin.exception.ValidationException;
import net.dzakirin.mapper.ProductMapper;
import net.dzakirin.model.Product;
import net.dzakirin.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static net.dzakirin.constant.ErrorCodes.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public BaseListResponse<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return BaseListResponse.<ProductResponse>builder()
                .success(true)
                .message("Products fetched successfully")
                .data(ProductMapper.toResponseList(products.getContent()))
                .totalRecords(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
    }

    public BaseResponse<ProductResponse> getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString())));

        return BaseResponse.<ProductResponse>builder()
                .success(true)
                .message("Product found")
                .data(ProductMapper.toProductResponse(product))
                .build();
    }

    @Transactional
    public BaseResponse<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = ProductMapper.toProduct(productRequest);
        productRepository.save(product);

        return BaseResponse.<ProductResponse>builder()
                .success(true)
                .message("Product created successfully")
                .data(ProductMapper.toProductResponse(product))
                .build();
    }

    @Transactional
    public BaseResponse<ProductResponse> updateProduct(UUID productId, ProductRequest updatedProductRequest) {
        productRequestValidation(updatedProductRequest);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString())));

        product.setTitle(updatedProductRequest.getTitle());
        product.setPrice(updatedProductRequest.getPrice());
        product.setStock(updatedProductRequest.getStock());
        productRepository.save(product);

        return BaseResponse.<ProductResponse>builder()
                .success(true)
                .message("Product updated successfully")
                .data(ProductMapper.toProductResponse(product))
                .build();
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString()));
        }
        productRepository.deleteById(productId);
    }

    private void productRequestValidation(ProductRequest productRequest) {
        if (productRequest.getTitle() == null || productRequest.getTitle().trim().isEmpty()) {
            throw new ValidationException(PRODUCT_TITLE_EMPTY.getMessage());
        }
        if (productRequest.getTitle().length() < 3 || productRequest.getTitle().length() > 255) {
            throw new ValidationException(PRODUCT_TITLE_CHARACTER_VALIDATION.getMessage());
        }
        if (productRequest.getPrice() == null || productRequest.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(PRODUCT_PRICE_VALIDATION.getMessage());
        }
        if (productRequest.getStock() == null || productRequest.getStock() < 0) {
            throw new ValidationException(PRODUCT_STOCK_VALIDATION.getMessage());
        }
    }

}
