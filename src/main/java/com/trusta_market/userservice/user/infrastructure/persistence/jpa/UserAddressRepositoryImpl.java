package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import com.trusta_market.userservice.user.application.port.out.UserAddressRepository;
import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public List<UserAddress> findAllActiveAddressesByUserId(UserId userId) {
        return userAddressJpaRepository.findAllByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public long countActiveAddressesByUserId(UserId userId) {
        return userAddressJpaRepository.countByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Optional<UserAddress> findActiveAddressByIdAndUserId(AddressId addressId, UserId userId) {
        return userAddressJpaRepository.findByAddressIdAndUserIdAndDeletedAtIsNull(addressId.value(), userId);
    }

    @Transactional
    @Override
    public void clearDefaultAddress(UserId userId) {
        userAddressJpaRepository.findAllByUserIdAndDeletedAtIsNullAndIsDefaultTrue(userId)
                .forEach(address -> address.unmarkDefaultAddress());
    }

    // 인터페이스에 없는 메서드들이지만 필요하다면 남겨둘 수 있으나 @Override는 제거해야 함
    public Optional<UserAddress> findById(AddressId addressId) {
        return userAddressJpaRepository.findById(addressId.value());
    }

    public Optional<UserAddress> findDefaultAddressByUserId(UserId userId) {
        return userAddressJpaRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(userId);
    }
}
