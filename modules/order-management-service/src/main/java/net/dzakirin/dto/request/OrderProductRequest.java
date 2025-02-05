package net.dzakirin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderProductRequest {
    @NotNull
    private UUID productId;

    @Min(1)
    private int quantity;
}
