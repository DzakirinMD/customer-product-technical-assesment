package net.dzakirin.constant;

public enum ErrorCodes {

    // Product error
    PRODUCT_NOT_FOUND("Product not found with ID: %s"),
    PRODUCT_LIST_INVALID("Some of these product IDs are invalid : %s"),
    PRODUCT_TITLE_EMPTY("Product title cannot be empty"),
    PRODUCT_TITLE_CHARACTER_VALIDATION("Product title must be between 3 and 255 characters"),
    PRODUCT_PRICE_VALIDATION("Product price must be greater than or equal to zero"),
    PRODUCT_STOCK_VALIDATION("Product stock must be greater than or equal to zero"),

    // Order error
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
