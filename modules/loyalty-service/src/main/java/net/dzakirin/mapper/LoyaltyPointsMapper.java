package net.dzakirin.mapper;

import lombok.experimental.UtilityClass;
import net.dzakirin.dto.response.LoyaltyPointsResponse;
import net.dzakirin.model.LoyaltyPoints;

import java.util.List;

@UtilityClass
public class LoyaltyPointsMapper {

    public static LoyaltyPointsResponse toLoyaltyPointsResponse(LoyaltyPoints loyaltyPoints) {
        if (loyaltyPoints == null) {
            return null;
        }

        return LoyaltyPointsResponse.builder()
                .id(loyaltyPoints.getId())
                .customerId(loyaltyPoints.getCustomerId())
                .totalPoints(loyaltyPoints.getTotalPoints())
                .lastUpdated(loyaltyPoints.getLastUpdated())
                .build();
    }

    public static List<LoyaltyPointsResponse> toResponseList(List<LoyaltyPoints> customers) {
        return customers.stream()
                .map(LoyaltyPointsMapper::toLoyaltyPointsResponse)
                .toList();
    }
}
