package com.trusta_market.userservice.trustscore.infrastructure.persistence.jpa;

import com.trusta_market.userservice.trustscore.domain.TrustScore;
import com.trusta_market.userservice.trustscore.domain.repository.TrustScoreRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TrustScoreRepositoryImpl implements TrustScoreRepository {

    private final TrustScoreJpaRepository trustScoreJpaRepository;

    public TrustScoreRepositoryImpl(TrustScoreJpaRepository trustScoreJpaRepository) {
        this.trustScoreJpaRepository = trustScoreJpaRepository;
    }

    @Override
    public TrustScore save(TrustScore trustScore) {
        return trustScoreJpaRepository.save(trustScore);
    }

    @Override
    public Optional<TrustScore> findById(UUID trustScoreId) {
        return trustScoreJpaRepository.findById(trustScoreId);
    }

    @Override
    public Optional<TrustScore> findByUserId(UUID userId) {
        return trustScoreJpaRepository.findByUserId(userId);
    }
}
