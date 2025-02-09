package net.dzakirin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.dto.response.LoyaltyPointsResponse;
import net.dzakirin.service.LoyaltyPointsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loyalty-points")
public class LoyaltyPointsController {

    private final LoyaltyPointsService loyaltyPointsService;

    @Operation(summary = "Get all loyalty points with pagination")
    @GetMapping
    public ResponseEntity<BaseListResponse<LoyaltyPointsResponse>> getAllLoyaltyPoints(
            @Parameter(description = "Page number (starts from 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Page size", example = "5")
            @RequestParam(defaultValue = "5") int size,

            @Parameter(description = "Sorting field",
                    array = @ArraySchema(schema = @Schema(allowableValues = {"totalPoints", "lastUpdated"},
                            type = "string")))
            @RequestParam(defaultValue = "lastUpdated") String[] sort) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).descending());
        return ResponseEntity.ok(loyaltyPointsService.getAllLoyaltyPoints(pageable));
    }

    @Operation(summary = "Get loyalty points by Customer ID")
    @GetMapping("/{customerId}")
    public ResponseEntity<BaseResponse<LoyaltyPointsResponse>> getLoyaltyPointsByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(loyaltyPointsService.getLoyaltyPointsByCustomerId(customerId));
    }
}
