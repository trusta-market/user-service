package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseCreatedEntity;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
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
@Table(name = "user_status_history")
public class UserStatusHistory extends BaseCreatedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID statusHistoryId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus prevStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus nextStatus;

    @Column(length = 200)
    private String reason;

    @Builder
    public UserStatusHistory(UUID statusHistoryId, UUID userId, UserStatus prevStatus, UserStatus nextStatus, String reason) {
        this.statusHistoryId = statusHistoryId;
        this.userId = userId;
        this.prevStatus = prevStatus;
        this.nextStatus = nextStatus;
        this.reason = reason;
    }

    public static UserStatusHistory create(UUID userId, UserStatus prevStatus, UserStatus nextStatus, String reason) {
        return UserStatusHistory.builder()
                .userId(userId)
                .prevStatus(prevStatus)
                .nextStatus(nextStatus)
                .reason(reason)
                .build();
    }
}
