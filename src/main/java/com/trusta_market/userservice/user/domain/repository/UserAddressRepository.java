package com.trusta_market.userservice.user.domain.repository;

import com.trusta_market.userservice.user.domain.entity.UserAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository {
    UserAddress save(UserAddress address);
    Optional<UserAddress> findById(UUID addressId);
    List<UserAddress> findAllByUserIdAndDeletedAtIsNull(UUID userId);
    long countByUserIdAndDeletedAtIsNull(UUID userId);
    Optional<UserAddress> findByAddressIdAndUserIdAndDeletedAtIsNull(UUID addressId, UUID userId);
    Optional<UserAddress> findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UUID userId);
    void clearDefaultAddress(UUID userId);
}
