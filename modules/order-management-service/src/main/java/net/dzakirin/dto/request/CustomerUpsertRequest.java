package net.dzakirin.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * This request DTO is being validate by customerRequestCreationValidation instead of jakarta validation
 */
@Data
@Builder
public class CustomerUpsertRequest {
    private String firstName;
    private String lastName;
    private String email;
}
