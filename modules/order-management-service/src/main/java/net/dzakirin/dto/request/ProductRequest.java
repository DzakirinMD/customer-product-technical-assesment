package net.dzakirin.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * This request DTO is being validate by productRequestValidation instead of jakarta validation
 */
@Data
@Builder
public class ProductRequest {
    private String title;
    private BigDecimal price;
    private Integer stock;
}
