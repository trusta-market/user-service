package com.trusta_market.userservice.suspension.domain.repository;

import com.trusta_market.userservice.suspension.domain.UserSuspension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserSuspensionRepository {
    UserSuspension save(UserSuspension suspension);
    Optional<UserSuspension> findById(UUID suspensionId);
    List<UserSuspension> findAllByUserId(UUID userId);
}
