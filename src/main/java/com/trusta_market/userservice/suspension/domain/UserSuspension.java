package com.trusta_market.userservice.suspension.domain;

import com.trustamarket.common.domain.BaseCreatedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_suspension")
public class UserSuspension extends BaseCreatedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID suspensionId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 200)
    private String reason;

    private LocalDateTime expiresAt;

    private LocalDateTime releasedAt;

    private UUID releasedBy;

    @Column(length = 200)
    private String releasedReason;

    @Builder
    public UserSuspension(UUID suspensionId, UUID userId, String reason, LocalDateTime expiresAt, LocalDateTime releasedAt, UUID releasedBy, String releasedReason) {
        this.suspensionId = suspensionId;
        this.userId = userId;
        this.reason = reason;
        this.expiresAt = expiresAt;
        this.releasedAt = releasedAt;
        this.releasedBy = releasedBy;
        this.releasedReason = releasedReason;
    }

    public static UserSuspension create(UUID userId, String reason, LocalDateTime expiresAt) {
        return UserSuspension.builder()
                .userId(userId)
                .reason(reason)
                .expiresAt(expiresAt)
                .build();
    }

    public void release(UUID releasedBy, String releasedReason) {
        this.releasedAt = LocalDateTime.now();
        this.releasedBy = releasedBy;
        this.releasedReason = releasedReason;
    }
}
