package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserMembershipHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserMembershipHistoryJpaRepository extends JpaRepository<UserMembershipHistory, UUID> {

    List<UserMembershipHistory> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
