package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseCreatedEntity;
import com.trusta_market.userservice.user.domain.vo.PointRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "user_membership_point_history",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_order_role",
                columnNames = {"order_id", "role"}          // Kafka 중복 이벤트 멱등성 보장
        )
)
public class UserMembershipPointHistory extends BaseCreatedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID membershipPointHistoryId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PointRole role;                                  // BUYER / SELLER

    @Column(nullable = false)
    private int earnedPoints;

    @Builder
    public UserMembershipPointHistory(UUID userId, UUID orderId, PointRole role, int earnedPoints) {
        this.userId = userId;
        this.orderId = orderId;
        this.role = role;
        this.earnedPoints = earnedPoints;
    }

    public static UserMembershipPointHistory create(UUID userId, UUID orderId, PointRole role, int earnedPoints) {
        return UserMembershipPointHistory.builder()
                .userId(userId)
                .orderId(orderId)
                .role(role)
                .earnedPoints(earnedPoints)
                .build();
    }
}
