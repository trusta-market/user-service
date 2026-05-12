package com.trusta_market.userservice.trustscore.infrastructure.persistence.jpa;

import com.trusta_market.userservice.trustscore.domain.TrustScore;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrustScoreJpaRepository extends JpaRepository<TrustScore, UUID> {
    Optional<TrustScore> findByUserId(UserId userId);
}
