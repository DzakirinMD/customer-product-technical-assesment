package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import net.dzakirin.ErrorCodes;
import net.dzakirin.common.dto.response.BaseListResponse;
import net.dzakirin.common.dto.response.BaseResponse;
import net.dzakirin.exception.ResourceNotFoundException;
import net.dzakirin.dto.response.LoyaltyPointsResponse;
import net.dzakirin.mapper.LoyaltyPointsMapper;
import net.dzakirin.model.LoyaltyPoints;
import net.dzakirin.repository.LoyaltyPointsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoyaltyPointsService {

    private final LoyaltyPointsRepository loyaltyPointsRepository;

    public BaseListResponse<LoyaltyPointsResponse> getAllLoyaltyPoints(Pageable pageable) {
        Page<LoyaltyPoints> loyaltyPointsPage = loyaltyPointsRepository.findAll(pageable);

        return BaseListResponse.<LoyaltyPointsResponse>builder()
                .success(true)
                .message("Loyalty Points fetched successfully")
                .data(LoyaltyPointsMapper.toResponseList(loyaltyPointsPage.getContent()))
                .totalRecords(loyaltyPointsPage.getTotalElements())
                .totalPages(loyaltyPointsPage.getTotalPages())
                .build();
    }

    public BaseResponse<LoyaltyPointsResponse> getLoyaltyPointsByCustomerId(UUID customerId) {
        LoyaltyPoints product = loyaltyPointsRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodes.LOYALTY_POINTS_NOT_FOUND.getMessage(customerId.toString())));

        return BaseResponse.<LoyaltyPointsResponse>builder()
                .success(true)
                .message("Loyalty Points found")
                .data(LoyaltyPointsMapper.toLoyaltyPointsResponse(product))
                .build();
    }
}
