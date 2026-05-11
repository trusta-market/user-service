package com.trusta_market.userservice.trustscore.domain.repository;

import com.trusta_market.userservice.trustscore.domain.TrustScore;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.Optional;
import java.util.UUID;

public interface TrustScoreRepository {
    TrustScore save(TrustScore trustScore);
    Optional<TrustScore> findById(UUID trustScoreId);
    Optional<TrustScore> findByUserId(UserId userId);
}
