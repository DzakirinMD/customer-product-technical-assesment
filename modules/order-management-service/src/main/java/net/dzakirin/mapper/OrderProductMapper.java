package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.dto.request.OrderProductRequest;
import net.dzakirin.dto.request.OrderRequest;
import net.dzakirin.model.Order;
import net.dzakirin.model.OrderProduct;
import net.dzakirin.model.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class OrderProductMapper {

    public static List<OrderProduct> toOrderProductList(OrderRequest orderRequest, Order order, Map<UUID, Product> productMap) {
        return orderRequest.getOrderProducts().stream()
                .map(request -> toOrderProduct(request, order, productMap))
                .toList();
    }

    private static OrderProduct toOrderProduct(OrderProductRequest request, Order order, Map<UUID, Product> productMap) {
        Product product = productMap.get(request.getProductId());
        return OrderProduct.builder()
                .product(product)
                .quantity(request.getQuantity())
                .order(order)
                .build();
    }
}
