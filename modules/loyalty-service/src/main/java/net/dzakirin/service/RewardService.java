package net.dzakirin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dzakirin.client.EmailClient;
import net.dzakirin.common.dto.event.OrderEvent;
import net.dzakirin.constant.LoyaltyTransactionType;
import net.dzakirin.model.LoyaltyPoints;
import net.dzakirin.model.LoyaltyRules;
import net.dzakirin.model.LoyaltyTransaction;
import net.dzakirin.repository.LoyaltyPointsRepository;
import net.dzakirin.repository.LoyaltyRulesRepository;
import net.dzakirin.repository.LoyaltyTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final LoyaltyPointsRepository loyaltyPointsRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;
    private final LoyaltyRulesRepository loyaltyRulesRepository;
    private final EmailClient emailClient;

    @Transactional
    public void processLoyaltyPoints(OrderEvent orderEvent) {
        log.info("Processing loyalty points for OrderID={}, CustomerID={}",
                orderEvent.getId(), orderEvent.getCustomerId());

        // Fetch or create customer’s loyalty points
        LoyaltyPoints loyaltyPoints = loyaltyPointsRepository.findByCustomerId(orderEvent.getCustomerId())
                .orElseGet(() -> {
                    LoyaltyPoints newLoyalty = LoyaltyPoints.builder()
                            .customerId(orderEvent.getCustomerId())
                            .totalPoints(0)
                            .lastUpdated(LocalDateTime.now())
                            .build();
                    return loyaltyPointsRepository.save(newLoyalty);
                });

        // Calculate the total order amount
        BigDecimal orderTotal = calculateOrderTotal(orderEvent);

        // Dynamically fetch applicable rule
        int pointsEarned = calculateLoyaltyPoints(orderTotal);

        if (pointsEarned > 0) {
            // Save transaction
            LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                    .loyaltyPoints(loyaltyPoints)
                    .orderId(orderEvent.getId())
                    .pointsAwarded(pointsEarned)
                    .transactionType(LoyaltyTransactionType.EARNED)
                    .transactionDate(LocalDateTime.now())
                    .build();
            loyaltyTransactionRepository.save(transaction);

            // Update total loyalty points
            loyaltyPoints.setTotalPoints(loyaltyPoints.getTotalPoints() + pointsEarned);
            loyaltyPoints.setLastUpdated(LocalDateTime.now());
            loyaltyPointsRepository.save(loyaltyPoints);

            log.info("Loyalty points updated: CustomerID={}, New Total={}", orderEvent.getCustomerId(), loyaltyPoints.getTotalPoints());

            // Send loyalty points email
            emailClient.sendLoyaltyPointsEmail(orderEvent.getCustomerId(), orderEvent.getCustomerEmail(), pointsEarned);
        } else {
            log.info("No loyalty points awarded for OrderID={}", orderEvent.getId());
        }
    }

    /**
     * Dynamically calculate loyalty points based on the order total using the latest rule.
     */
    private int calculateLoyaltyPoints(BigDecimal orderTotal) {
        Optional<LoyaltyRules> rule = loyaltyRulesRepository
                .findFirstByMinOrderAmountLessThanEqualAndRuleActiveTrue(orderTotal);

        return rule.map(loyaltyRule -> {
            BigDecimal multiplier = orderTotal.divide(loyaltyRule.getMinOrderAmount(), BigDecimal.ROUND_DOWN);
            return multiplier.multiply(BigDecimal.valueOf(loyaltyRule.getPointsAwarded())).intValue();
        }).orElse(0);
    }

    /**
     * Calculate the total order amount.
     */
    private BigDecimal calculateOrderTotal(OrderEvent orderEvent) {
        return orderEvent.getOrderProducts().stream()
                .map(orderProduct -> orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
