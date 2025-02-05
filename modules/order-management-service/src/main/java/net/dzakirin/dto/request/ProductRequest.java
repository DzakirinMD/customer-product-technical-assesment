package net.dzakirin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {
    @NotBlank
    private String title;

    @NotNull
    private BigDecimal price;

    @Min(0)
    private Integer stock;
}
