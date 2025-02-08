package net.dzakirin.model;

import jakarta.persistence.*;
import lombok.*;
import net.dzakirin.constant.LoyaltyTransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loyalty_transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "loyalty_points_id", nullable = false)
    private LoyaltyPoints loyaltyPoints;

    @Column
    private UUID orderId;

    @Column(nullable = false)
    private int pointsAwarded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoyaltyTransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
