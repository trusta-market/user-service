package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserMembershipHistory;
import com.trusta_market.userservice.user.application.port.out.UserMembershipHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserMembershipHistoryRepositoryImpl implements UserMembershipHistoryRepository {

    private final UserMembershipHistoryJpaRepository userMembershipHistoryJpaRepository;

    public UserMembershipHistoryRepositoryImpl(UserMembershipHistoryJpaRepository userMembershipHistoryJpaRepository) {
        this.userMembershipHistoryJpaRepository = userMembershipHistoryJpaRepository;
    }

    @Override
    public UserMembershipHistory save(UserMembershipHistory history) {
        return userMembershipHistoryJpaRepository.save(history);
    }

    @Override
    public List<UserMembershipHistory> findAllByUserId(UUID userId) {
        return userMembershipHistoryJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}
