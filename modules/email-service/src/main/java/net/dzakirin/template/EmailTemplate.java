package net.dzakirin.template;

import lombok.experimental.UtilityClass;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.common.dto.request.EmailRequest;

@UtilityClass
public class EmailTemplate {

    public static String orderConfirmationTemplate(OrderEvent orderEvent, double totalAmount) {
        StringBuilder orderProductsInfo = new StringBuilder();
        orderEvent.getOrderProducts().forEach(product ->
                orderProductsInfo.append("  - %s (Qty: %d) - RM %.2f\n".formatted(
                        product.getProductTitle(), product.getQuantity(), product.getPrice()))
        );

        return """
                Dear Valued Customer,

                Thank you for your order! We are pleased to inform you that your order has been successfully processed.

                **📦 Order Details:**
                ─────────────────────────────────
                **Order ID:** %s
                **Order Date:** %s
                **Customer ID:** %s
                ─────────────────────────────────

                **🛍️ Items Ordered:**
                %s

                **💳 Total Amount:** RM %.2f

                Your order is now being prepared. We will notify you once it's shipped.

                **Need Help?**  
                If you have any questions or need assistance, feel free to reach out to our support team.

                Thank you for shopping with us! 🛍️🎉  
                We appreciate your business and hope to serve you again soon.

                **Best regards,**  
                🏪 Order Management Team
                """.formatted(
                orderEvent.getId(),
                orderEvent.getOrderDate(),
                orderEvent.getCustomerId(),
                orderProductsInfo.toString(),
                totalAmount
        );
    }

    public static String loyaltyPointsTemplate(EmailRequest emailRequest) {
        return """
                Dear Valued Customer,
                
                🎉 Congratulations! You've just earned %d loyalty points!
                
                These points have been added to your loyalty account and can be redeemed for exciting rewards.
                
                Keep shopping with us and enjoy exclusive benefits!
                
                Thank you for your continued support.
                
                Best regards,
                Loyalty Rewards Team
                """.formatted(emailRequest.points());
    }
}

