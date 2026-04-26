package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, UUID> {
    List<UserAddress> findAllByUserIdAndDeletedAtIsNull(UUID userId);
    long countByUserIdAndDeletedAtIsNull(UUID userId);
    Optional<UserAddress> findByAddressIdAndUserIdAndDeletedAtIsNull(UUID addressId, UUID userId);
    Optional<UserAddress> findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UUID userId);
    List<UserAddress> findAllByUserIdAndDeletedAtIsNullAndIsDefaultTrue(UUID userId);
}
