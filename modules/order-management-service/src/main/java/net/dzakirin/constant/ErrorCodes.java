package net.dzakirin.constant;

public enum ErrorCodes {

    // Product error
    PRODUCT_NOT_FOUND("Product not found with ID: %s"),
    PRODUCT_LIST_INVALID("Some of these product IDs are invalid : %s"),

    // Order rrror
    ORDER_NOT_FOUND("Order not found with ID: %s"),

    // Customer error
    CUSTOMER_NOT_FOUND("Customer not found with ID: %s");

    private final String messageTemplate;

    ErrorCodes(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(String... args) {
        return String.format(messageTemplate, (Object[]) args);
    }
}
