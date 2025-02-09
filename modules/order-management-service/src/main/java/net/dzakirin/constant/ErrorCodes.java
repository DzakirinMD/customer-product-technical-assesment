package net.dzakirin.constant;

public enum ErrorCodes {

    // Product error
    PRODUCT_NOT_FOUND("Product not found with ID: %s"),
    PRODUCT_LIST_INVALID("Some of these product IDs are invalid : %s"),
    PRODUCT_TITLE_EMPTY("Product title cannot be empty"),
    PRODUCT_TITLE_CHARACTER_VALIDATION("Product title must be between 3 and 255 characters"),
    PRODUCT_PRICE_VALIDATION("Product price must be greater than or equal to zero"),
    PRODUCT_STOCK_VALIDATION("Product stock must be greater than or equal to zero"),
    INSUFFICIENT_STOCK("Insufficient stock for products: %s"),

    // Order error
    ORDER_NOT_FOUND("Order not found with ID: %s"),
    MINIMUM_ORDER_QUANTITY("Minimum order quantity is 1 for product: %s"),

    // Customer errors
    CUSTOMER_NOT_FOUND("Customer not found with ID: %s"),
    CUSTOMER_FIRST_NAME_EMPTY("Customer first name cannot be empty"),
    CUSTOMER_FIRST_NAME_LENGTH("Customer first name must be between 2 and 100 characters"),
    CUSTOMER_LAST_NAME_EMPTY("Customer last name cannot be empty"),
    CUSTOMER_LAST_NAME_LENGTH("Customer last name must be between 2 and 100 characters"),
    CUSTOMER_EMAIL_EMPTY("Customer email cannot be empty"),
    CUSTOMER_EMAIL_INVALID("Invalid email format"),
    CUSTOMER_EMAIL_ALREADY_EXISTS("Customer email is already registered");

    private final String messageTemplate;

    ErrorCodes(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(String... args) {
        return String.format(messageTemplate, (Object[]) args);
    }
}
