package net.dzakirin.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private String title;
    private BigDecimal price;
    private Integer stock;
}
