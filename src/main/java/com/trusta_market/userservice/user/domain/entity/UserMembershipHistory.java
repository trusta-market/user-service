package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseCreatedEntity;
import com.trusta_market.userservice.user.domain.vo.Membership;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_membership_history")
public class UserMembershipHistory extends BaseCreatedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID membershipHistoryId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Membership prevMembership;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Membership nextMembership;

    @Column(length = 200)
    private String reason;

    @Builder
    public UserMembershipHistory(UUID membershipHistoryId, UUID userId, Membership prevMembership, Membership nextMembership, String reason) {
        this.membershipHistoryId = membershipHistoryId;
        this.userId = userId;
        this.prevMembership = prevMembership;
        this.nextMembership = nextMembership;
        this.reason = reason;
    }

    public static UserMembershipHistory create(UUID userId, Membership prevMembership, Membership nextMembership, String reason) {
        return UserMembershipHistory.builder()
                .userId(userId)
                .prevMembership(prevMembership)
                .nextMembership(nextMembership)
                .reason(reason)
                .build();
    }
}
