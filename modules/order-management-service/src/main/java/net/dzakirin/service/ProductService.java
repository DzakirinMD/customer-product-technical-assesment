package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.constant.ErrorCodes;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.dto.response.ProductResponse;
import net.dzakirin.exception.ResourceNotFoundException;
import net.dzakirin.mapper.ProductMapper;
import net.dzakirin.model.Product;
import net.dzakirin.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductMapper::toResponse);
    }

    public Optional<ProductResponse> getProductById(UUID productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString())));
        return Optional.ofNullable(ProductMapper.toResponse(product));
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = ProductMapper.toEntity(productRequest);
        productRepository.save(product);
        return ProductMapper.toResponse(product);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString()));
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest updatedProductRequest) {
        return productRepository.findById(productId).map(product -> {
            product.setTitle(updatedProductRequest.getTitle());
            product.setPrice(updatedProductRequest.getPrice());
            product.setStock(updatedProductRequest.getStock());
            productRepository.save(product);
            return ProductMapper.toResponse(product);
        }).orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.PRODUCT_NOT_FOUND.getMessage(productId.toString())));
    }
}
