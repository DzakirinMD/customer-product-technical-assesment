package net.dzakirin;

public enum ErrorCodes {

    // Loyalty Points error
    LOYALTY_POINTS_NOT_FOUND("Loyalty Points not found with Customer ID: %s");

    private final String messageTemplate;

    ErrorCodes(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage(String... args) {
        return String.format(messageTemplate, (Object[]) args);
    }
}
