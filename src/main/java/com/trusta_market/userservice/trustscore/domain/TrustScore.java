package com.trusta_market.userservice.trustscore.domain;

import com.trusta_market.userservice.trustscore.domain.vo.Score;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trustamarket.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

// 유저 신뢰 점수 엔티티
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_trust_scores")
public class TrustScore extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID trustScoreId;

    @Column(nullable = false, unique = true)
    private UserId userId;

    @Column(nullable = false)
    private Score score;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public TrustScore(UUID trustScoreId, UserId userId, Score score, Integer version) {
        this.trustScoreId = trustScoreId;
        this.userId = userId;
        this.score = score;
        this.version = version;
    }

    // 신규 신뢰 점수 객체 생성 (초기 점수 지정 가능)
    public static TrustScore create(UserId userId, Score initialScore) {
        return TrustScore.builder()
                .userId(userId)
                .score(initialScore)
                .build();
    }

    // 점수 증감 로직
    public void addScore(long delta) {
        this.score = this.score.add(delta);
    }
}
