package com.trusta_market.userservice.suspension.infrastructure.persistence.jpa;

import com.trusta_market.userservice.suspension.domain.UserSuspension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSuspensionJpaRepository extends JpaRepository<UserSuspension, UUID> {
    List<UserSuspension> findAllByUserId(UUID userId);
}
