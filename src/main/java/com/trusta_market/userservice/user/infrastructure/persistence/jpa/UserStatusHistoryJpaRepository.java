package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// 유저 상태 변경 이력 Spring Data JPA 인터페이스
public interface UserStatusHistoryJpaRepository extends JpaRepository<UserStatusHistory, UUID> {

    List<UserStatusHistory> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
