package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface UserAddressJpaRepository extends JpaRepository<UserAddress, AddressId> {
    List<UserAddress> findAllByUserIdAndDeletedAtIsNull(UserId userId);
    long countByUserIdAndDeletedAtIsNull(UserId userId);
    Optional<UserAddress> findByAddressIdAndUserIdAndDeletedAtIsNull(AddressId addressId, UserId userId);
    Optional<UserAddress> findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UserId userId);
    List<UserAddress> findAllByUserIdAndDeletedAtIsNullAndIsDefaultTrue(UserId userId);
}
