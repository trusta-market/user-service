package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import com.trusta_market.userservice.user.domain.repository.UserAddressRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAddressRepositoryImpl implements UserAddressRepository {

    private final UserAddressJpaRepository userAddressJpaRepository;

    public UserAddressRepositoryImpl(UserAddressJpaRepository userAddressJpaRepository) {
        this.userAddressJpaRepository = userAddressJpaRepository;
    }

    @Override
    public UserAddress save(UserAddress address) {
        return userAddressJpaRepository.save(address);
    }

    @Override
    public Optional<UserAddress> findById(UUID addressId) {
        return userAddressJpaRepository.findById(addressId);
    }

    @Override
    public List<UserAddress> findAllByUserIdAndDeletedAtIsNull(UUID userId) {
        return userAddressJpaRepository.findAllByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public long countByUserIdAndDeletedAtIsNull(UUID userId) {
        return userAddressJpaRepository.countByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Optional<UserAddress> findByAddressIdAndUserIdAndDeletedAtIsNull(UUID addressId, UUID userId) {
        return userAddressJpaRepository.findByAddressIdAndUserIdAndDeletedAtIsNull(addressId, userId);
    }

    @Override
    public Optional<UserAddress> findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UUID userId) {
        return userAddressJpaRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(userId);
    }

    @Transactional
    @Override
    public void clearDefaultAddress(UUID userId) {
        userAddressJpaRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(userId)
                .ifPresent(address -> address.setDefault(false));
    }
}
