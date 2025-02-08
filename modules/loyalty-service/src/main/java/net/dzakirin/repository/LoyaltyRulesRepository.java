package net.dzakirin.repository;

import net.dzakirin.model.LoyaltyRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoyaltyRulesRepository extends JpaRepository<LoyaltyRules, UUID> {
    Optional<LoyaltyRules> findFirstByMinOrderAmountLessThanEqualAndRuleActiveTrue(BigDecimal orderTotal);
}
