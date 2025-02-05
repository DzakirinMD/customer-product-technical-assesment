package net.dzakirin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderRequest {
    @NotNull
    private UUID customerId;

    @NotNull
    private List<OrderProductRequest> orderProducts;
}
