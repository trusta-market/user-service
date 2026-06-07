package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.application.port.out.UserMembershipPointHistoryRepository;
import com.trusta_market.userservice.user.domain.entity.UserMembershipPointHistory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class UserMembershipPointHistoryRepositoryImpl implements UserMembershipPointHistoryRepository {

    private final UserMembershipPointHistoryJpaRepository jpaRepository;

    public UserMembershipPointHistoryRepositoryImpl(UserMembershipPointHistoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserMembershipPointHistory save(UserMembershipPointHistory history) {
        return jpaRepository.save(history);
    }

    @Override
    public int sumEarnedPointsSince(UUID userId, Instant since) {
        return jpaRepository.sumEarnedPointsSince(userId, since);
    }

    @Override
    public List<UUID> findUserIdsWithExpiringPoints(Instant from, Instant to) {
        return jpaRepository.findDistinctUserIdsByCreatedAtBetween(from, to);
    }
}
