package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.dto.request.ProductRequest;
import net.dzakirin.dto.response.ProductResponse;
import net.dzakirin.model.Product;

import java.util.List;

@UtilityClass
public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public static List<ProductResponse> toResponseList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .toList();
    }

    public static Product toProduct(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }

        return Product.builder()
                .title(productRequest.getTitle())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .build();
    }
}
