package com.trusta_market.userservice.trustscore.domain;


import com.trustamarket.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "trust_score")
public class TrustScore extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID trustScoreId;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private Long score;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public TrustScore(UUID trustScoreId, UUID userId, Long score, Integer version) {
        this.trustScoreId = trustScoreId;
        this.userId = userId;
        this.score = score != null ? score : 0L;
        this.version = version;
    }

    public static TrustScore create(UUID userId) {
        return new TrustScore(null, userId, 0L, null);
    }

    public void updateScore(Long score) {
        this.score = score;
    }
}
