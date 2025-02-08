package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.model.EmailDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final EmailService emailService;

    public void sendOrderConfirmationEmail(OrderEvent orderEvent) {
        log.info("Processing order data: CustomerId={}, OrderId={}",
                orderEvent.getCustomerId(),
                orderEvent.getId()
        );

        // Construct email message
        StringBuilder orderProductsInfo = new StringBuilder();
        orderEvent.getOrderProducts().forEach(product ->
                orderProductsInfo.append("- %s (Qty: %d)\n".formatted(product.getProductTitle(), product.getQuantity()))
        );

        String msgBody = """
            Hi,
            
            Your order has been successfully created!
            
            **Order Details:**
            Order ID: %s
            Order Date: %s
            Customer ID: %s
            
            **Products Ordered:**
            %s
            
            Thank you for shopping with us!
            
            Best regards,
            Order Management Team
            """.formatted(
                orderEvent.getId(),
                orderEvent.getOrderDate(),
                orderEvent.getCustomerId(),
                orderProductsInfo.toString()
        );

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(orderEvent.getCustomerEmail())
                .subject("🛒 Order Confirmation - Order ID: " + orderEvent.getId())
                .msgBody(msgBody)
                .build();

        emailService.sendEmail(emailDetails);
        log.info("Order confirmation email sent to {}", emailDetails.getRecipient());
    }

}
