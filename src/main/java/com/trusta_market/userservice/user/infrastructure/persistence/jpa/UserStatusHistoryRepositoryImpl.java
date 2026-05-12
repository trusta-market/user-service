package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;
import com.trusta_market.userservice.user.application.port.out.UserStatusHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

// UserStatusHistoryRepository 인터페이스의 JPA 기반 구현체
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

    public List<UserStatusHistory> findAllByUserId(UUID userId) {
        return userStatusHistoryJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}
