package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserStatusHistoryJpaRepository extends JpaRepository<UserStatusHistory, UUID> {

    List<UserStatusHistory> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
