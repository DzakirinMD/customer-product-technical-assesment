package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.common.dto.event.OrderProductEvent;
import net.dzakirin.dto.response.OrderProductResponse;
import net.dzakirin.dto.response.OrderResponse;
import net.dzakirin.model.Order;
import net.dzakirin.model.OrderProduct;

import java.util.List;

@UtilityClass
public class OrderMapper {

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null || order.getCustomer() == null) {
            return null;
        }

        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .orderDate(order.getOrderDate())
                .orderProducts(toOrderProductResponseList(order.getOrderProducts()))
                .build();
    }

    public static List<OrderResponse> toOrderResponseList(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    public static List<OrderProductResponse> toOrderProductResponseList(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderMapper::toOrderProductResponse)
                .toList();
    }

    public static OrderProductResponse toOrderProductResponse(OrderProduct orderProduct) {
        if (orderProduct == null || orderProduct.getProduct() == null) {
            return OrderProductResponse.builder()
                    .productId(null)
                    .productTitle("Unknown Product")
                    .quantity(0)
                    .build();
        }

        return OrderProductResponse.builder()
                .productId(orderProduct.getProduct().getId())
                .productTitle(orderProduct.getProduct().getTitle())
                .quantity(orderProduct.getQuantity())
                .build();
    }

    public static OrderEvent toOrderEvent(Order order) {
        if (order == null || order.getCustomer() == null) {
            return null;
        }

        return OrderEvent.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerEmail(order.getCustomer().getEmail())
                .orderDate(order.getOrderDate())
                .orderProducts(toOrderProductEventList(order.getOrderProducts()))
                .build();
    }

    public static List<OrderProductEvent> toOrderProductEventList(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderMapper::toOrderProductEvent)
                .toList();
    }

    public static OrderProductEvent toOrderProductEvent(OrderProduct orderProduct) {
        if (orderProduct == null || orderProduct.getProduct() == null) {
            return OrderProductEvent.builder()
                    .productId(null)
                    .productTitle("Unknown Product")
                    .quantity(0)
                    .build();
        }

        return OrderProductEvent.builder()
                .productId(orderProduct.getProduct().getId())
                .productTitle(orderProduct.getProduct().getTitle())
                .quantity(orderProduct.getQuantity())
                .build();
    }
}
