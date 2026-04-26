package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;
import com.trusta_market.userservice.user.domain.repository.UserStatusHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserStatusHistoryRepositoryImpl implements UserStatusHistoryRepository {

    private final UserStatusHistoryJpaRepository userStatusHistoryJpaRepository;

    public UserStatusHistoryRepositoryImpl(UserStatusHistoryJpaRepository userStatusHistoryJpaRepository) {
        this.userStatusHistoryJpaRepository = userStatusHistoryJpaRepository;
    }

    @Override
    public UserStatusHistory save(UserStatusHistory history) {
        return userStatusHistoryJpaRepository.save(history);
    }

    @Override
    public List<UserStatusHistory> findAllByUserId(UUID userId) {
        return userStatusHistoryJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}
