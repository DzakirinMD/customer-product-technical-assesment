package net.dzakirin.service

import net.dzakirin.constant.ErrorCodes
import net.dzakirin.dto.request.OrderProductRequest
import net.dzakirin.dto.request.OrderRequest
import net.dzakirin.common.dto.response.BaseListResponse
import net.dzakirin.common.dto.response.BaseResponse
import net.dzakirin.dto.response.OrderResponse
import net.dzakirin.exception.InsufficientStockException
import net.dzakirin.exception.ResourceNotFoundException
import net.dzakirin.exception.ValidationException
import net.dzakirin.mapper.OrderMapper
import net.dzakirin.model.Customer
import net.dzakirin.model.Order
import net.dzakirin.model.Product
import net.dzakirin.producer.OrderDataChangedProducer
import net.dzakirin.repository.CustomerRepository
import net.dzakirin.repository.OrderRepository
import net.dzakirin.repository.ProductRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class OrderServiceTest extends Specification {

    OrderRepository orderRepository = Mock()
    ProductRepository productRepository = Mock()
    CustomerRepository customerRepository = Mock()
    OrderDataChangedProducer orderDataChangedProducer = Mock()

    @Subject
    OrderService orderService

    def setup() {
        orderService = new OrderService(orderRepository, productRepository, customerRepository, orderDataChangedProducer)
    }

    def "getAllOrders: should return list of orders"() {
        given:
        def orders = [new Order(id: UUID.randomUUID(), orderDate: LocalDateTime.now())]
        def page = new PageImpl<>(orders)
        orderRepository.findAll(_ as Pageable) >> page

        when:
        BaseListResponse<OrderResponse> response = orderService.getAllOrders(Pageable.unpaged())

        then:
        response.success
        response.data.size() == 1
    }

    def "getOrderById: should return order if exists"() {
        given:
        def orderId = UUID.randomUUID()
        def customerId = UUID.randomUUID()
        def order = new Order(id: orderId, orderDate: LocalDateTime.now(), customer: new Customer(id: customerId))
        order.orderProducts = [] // Ensure this is not null

        def orderResponse = new OrderResponse(id: orderId, customerId: customerId, orderDate: order.orderDate, orderProducts: [])

        orderRepository.findById(orderId) >> Optional.of(order)

        // Explicitly mock the static method
        OrderMapper.metaClass.static.toOrderResponse = { Order o -> orderResponse }

        when:
        BaseResponse<OrderResponse> response = orderService.getOrderById(orderId)

        then:
        response.success
        response.data != null
    }

    def "getOrderById: should throw ResourceNotFoundException if order does not exist"() {
        given:
        def orderId = UUID.randomUUID()
        orderRepository.findById(orderId) >> Optional.empty()

        when:
        orderService.getOrderById(orderId)

        then:
        thrown(ResourceNotFoundException)
    }

    def "createOrder: should create order successfully"() {
        given:
        def customerId = UUID.randomUUID()
        def productId = UUID.randomUUID()
        def orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderProducts([
                        OrderProductRequest.builder()
                                .productId(productId)
                                .quantity(2)
                                .build()
                ])
                .build()

        def customer = new Customer(id: customerId)
        def product = new Product(id: productId, stock: 10)

        customerRepository.findById(customerId) >> Optional.of(customer)
        productRepository.findAllById([productId]) >> [product]
        orderRepository.save(_ as Order) >> { Order o -> o.id = UUID.randomUUID(); return o }

        when:
        BaseResponse<OrderResponse> response = orderService.createOrder(orderRequest)

        then:
        response.success
        response.message == "Order created successfully"
    }

    def "createOrder: should throw ResourceNotFoundException if customer does not exist"() {
        given:
        def customerId = UUID.randomUUID()
        def productId = UUID.randomUUID()

        def orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderProducts([
                        OrderProductRequest.builder().productId(productId).quantity(1).build()
                ])
                .build()

        def product = Product.builder().id(productId).stock(10).build()

        productRepository.findAllById([productId]) >> [product] // Mock product retrieval
        customerRepository.findById(customerId) >> Optional.empty() // Simulate customer not found

        when:
        orderService.createOrder(orderRequest)

        then:
        thrown(ResourceNotFoundException)
    }

    def "createOrder: should throw InsufficientStockException if product stock is insufficient"() {
        given:
        def customerId = UUID.randomUUID()
        def productId = UUID.randomUUID()
        def orderRequest = new OrderRequest(customerId: customerId, orderProducts: [new OrderProductRequest(productId: productId, quantity: 5)])
        def customer = new Customer(id: customerId)
        def product = new Product(id: productId, stock: 2)
        customerRepository.findById(customerId) >> Optional.of(customer)
        productRepository.findAllById([productId]) >> [product]

        when:
        orderService.createOrder(orderRequest)

        then:
        thrown(InsufficientStockException)
    }

    def "createOrder: should throw ResourceNotFoundException if some products do not exist"() {
        given:
        def customerId = UUID.randomUUID()
        def productId1 = UUID.randomUUID()
        def productId2 = UUID.randomUUID() // This product does not exist in DB

        def orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderProducts([
                        OrderProductRequest.builder().productId(productId1).quantity(2).build(),
                        OrderProductRequest.builder().productId(productId2).quantity(3).build()
                ])
                .build()

        def customer = Customer.builder().id(customerId).build()
        def product1 = Product.builder().id(productId1).stock(10).build()

        customerRepository.findById(customerId) >> Optional.of(customer)
        productRepository.findAllById([productId1, productId2]) >> [product1] // Only product1 is found

        when:
        orderService.createOrder(orderRequest)

        then:
        def ex = thrown(ResourceNotFoundException)
        ex.message == ErrorCodes.PRODUCT_LIST_INVALID.getMessage([productId2].toString())
    }

    def "createOrder: should throw ValidationException if any product has quantity less than 1"() {
        given:
        def customerId = UUID.randomUUID()
        def productId1 = UUID.randomUUID()
        def productId2 = UUID.randomUUID()

        def orderRequest = OrderRequest.builder()
                .customerId(customerId)
                .orderProducts([
                        OrderProductRequest.builder().productId(productId1).quantity(2).build(),
                        OrderProductRequest.builder().productId(productId2).quantity(0).build() // Invalid quantity
                ])
                .build()

        when:
        orderService.createOrder(orderRequest)

        then:
        def ex = thrown(ValidationException)
        ex.message == ErrorCodes.MINIMUM_ORDER_QUANTITY.getMessage([productId2].toString())
    }

}
