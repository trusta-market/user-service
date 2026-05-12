package com.trusta_market.userservice.trustscore.application.port.in;

import java.util.UUID;

// 유저 신뢰도 관리 유스케이스 인터페이스
public interface TrustScoreUseCase {
    // 유저 신뢰 점수 초기화
    void initializeScore(UUID userId);
    // 유저 신뢰 점수 변경 (가점/감점)
    void updateScore(UUID userId, int delta);
}
