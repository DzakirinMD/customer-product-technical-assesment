package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.constant.ErrorCodes;
import net.dzakirin.constant.EventType;
import net.dzakirin.dto.request.OrderProductRequest;
import net.dzakirin.dto.request.OrderRequest;
import net.dzakirin.dto.response.BaseListResponse;
import net.dzakirin.dto.response.BaseResponse;
import net.dzakirin.dto.response.OrderResponse;
import net.dzakirin.exception.InsufficientStockException;
import net.dzakirin.exception.ResourceNotFoundException;
import net.dzakirin.exception.ValidationException;
import net.dzakirin.mapper.OrderMapper;
import net.dzakirin.mapper.OrderProductMapper;
import net.dzakirin.model.Customer;
import net.dzakirin.model.Order;
import net.dzakirin.model.OrderProduct;
import net.dzakirin.model.Product;
import net.dzakirin.producer.OrderDataChangedProducer;
import net.dzakirin.repository.CustomerRepository;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.repository.OrderRepository;
import net.dzakirin.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.dzakirin.constant.ErrorCodes.MINIMUM_ORDER_QUANTITY;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderDataChangedProducer orderDataChangedProducer;

    public BaseListResponse<OrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = OrderMapper.toOrderResponseList(orders.getContent());

        return BaseListResponse.<OrderResponse>builder()
                .success(true)
                .message("Orders fetched successfully")
                .data(orderResponses)
                .totalRecords(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    public BaseResponse<OrderResponse> getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.ORDER_NOT_FOUND.getMessage(orderId.toString())));

        return BaseResponse.<OrderResponse>builder()
                .success(true)
                .message("Order found")
                .data(OrderMapper.toOrderResponse(order))
                .build();
    }

    @Transactional
    public BaseResponse<OrderResponse> createOrder(OrderRequest orderRequest) {
        // Fetch Products in Batch (to minimize DB calls)
        Map<UUID, Product> productMap = getProducts(orderRequest.getOrderProducts());

        // Validate Stock Availability
        validateStockAvailability(orderRequest, productMap);

        // Fetch Customer
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCodes.CUSTOMER_NOT_FOUND.getMessage(orderRequest.getCustomerId().toString())));

        // Create Order
        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .build();

        // Convert OrderRequest to OrderProducts using the fetched product map
        List<OrderProduct> orderProducts = OrderProductMapper.toOrderProductList(orderRequest, order, productMap);
        order.setOrderProducts(orderProducts);

        // Deduct Stock
        deductStock(orderProducts);

        // Save and Publish event
        orderRepository.save(order);
        OrderEvent orderEvent = OrderMapper.toOrderEvent(order);
        orderDataChangedProducer.publishEvent(orderEvent.getId().toString(), orderEvent, EventType.ORDER_CREATED.getEventName());

        return BaseResponse.<OrderResponse>builder()
                .success(true)
                .message("Order created successfully")
                .data(OrderMapper.toOrderResponse(order))
                .build();
    }

    /**
     * Fetch all products from database in a single query to reduce DB calls.
     */
    private Map<UUID, Product> getProducts(List<OrderProductRequest> orderProducts) {
        // Collect all product IDs with quantity less than 1 for validation
        List<UUID> invalidProductIds = orderProducts.stream()
                .filter(orderProductRequest -> orderProductRequest.getQuantity() < 1)
                .map(OrderProductRequest::getProductId)
                .toList();
        if (!invalidProductIds.isEmpty()) {
            throw new ValidationException(MINIMUM_ORDER_QUANTITY.getMessage(invalidProductIds.toString()));
        }

        List<UUID> productIds = orderProducts.stream()
                .map(OrderProductRequest::getProductId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // Identify missing product IDs
        List<UUID> missingProductIds = productIds.stream()
                .filter(id -> !productMap.containsKey(id))
                .toList();

        // Throw error if there are missing product IDs
        if (!missingProductIds.isEmpty()) {
            throw new ResourceNotFoundException(
                    ErrorCodes.PRODUCT_LIST_INVALID.getMessage(missingProductIds.toString())
            );
        }

        return productMap;
    }

    /**
     * Validate if requested product stock is sufficient.
     */
    private void validateStockAvailability(OrderRequest orderRequest, Map<UUID, Product> productMap) {
        List<UUID> insufficientStockProducts = orderRequest.getOrderProducts().stream()
                .filter(request -> {
                    Product product = productMap.get(request.getProductId());
                    return product.getStock() < request.getQuantity();
                })
                .map(OrderProductRequest::getProductId)
                .toList();

        if (!insufficientStockProducts.isEmpty()) {
            throw new InsufficientStockException(
                    ErrorCodes.INSUFFICIENT_STOCK.getMessage(insufficientStockProducts.toString())
            );
        }
    }

    /**
     * Deduct stock for ordered products.
     */
    private void deductStock(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            Product product = orderProduct.getProduct();
            product.setStock(product.getStock() - orderProduct.getQuantity());
        }
        productRepository.saveAll(orderProducts.stream().map(OrderProduct::getProduct).toList());
    }
}
