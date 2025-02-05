package net.dzakirin.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderProductResponse {
    private UUID productId;
    private String productTitle;
    private int quantity;
}
