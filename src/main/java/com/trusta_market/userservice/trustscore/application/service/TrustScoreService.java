package com.trusta_market.userservice.trustscore.application.service;

import com.trusta_market.userservice.trustscore.application.port.in.TrustScoreUseCase;
import com.trusta_market.userservice.trustscore.domain.TrustScore;
import com.trusta_market.userservice.trustscore.domain.repository.TrustScoreRepository;
import com.trusta_market.userservice.trustscore.domain.vo.Score;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

// 유저 신뢰도 관리 비즈니스 로직 서비스
@Service
@Transactional
public class TrustScoreService implements TrustScoreUseCase {

    private final TrustScoreRepository trustScoreRepository;

    public TrustScoreService(TrustScoreRepository trustScoreRepository) {
        this.trustScoreRepository = trustScoreRepository;
    }

    // 신규 유저 신뢰 점수 초기화
    @Override
    public void initializeScore(UUID userId) {
        UserId voUserId = UserId.of(userId);
        TrustScore score = TrustScore.create(voUserId, Score.of(50)); // 기본 50점 시작
        trustScoreRepository.save(score);
    }

    // 유저 신뢰 점수 증감 업데이트
    @Override
    public void updateScore(UUID userId, int delta) {
        UserId voUserId = UserId.of(userId);
        TrustScore score = trustScoreRepository.findByUserId(voUserId)
                .orElseGet(() -> TrustScore.create(voUserId, Score.of(50)));
        score.addScore(delta); // 점수 합산
        trustScoreRepository.save(score);
    }
}
