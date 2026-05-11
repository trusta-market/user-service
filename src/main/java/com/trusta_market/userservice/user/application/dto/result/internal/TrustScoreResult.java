package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.trustscore.domain.TrustScore;

import java.util.UUID;

public record TrustScoreResult(
        UUID userId,
        long score
) {
    // TrustScore 애그리거트로부터 신뢰 점수 결과로 변환한다.
    public static TrustScoreResult from(TrustScore trustScore) {
        return new TrustScoreResult(
                trustScore.getUserId().value(),
                trustScore.getScore().value()
        );
    }
}
