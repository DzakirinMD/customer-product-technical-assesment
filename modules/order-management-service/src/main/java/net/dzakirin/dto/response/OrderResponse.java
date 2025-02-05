package net.dzakirin.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {
    private UUID id;
    private LocalDateTime orderDate;
    private UUID customerId;
    private List<OrderProductResponse> orderProducts;
}
