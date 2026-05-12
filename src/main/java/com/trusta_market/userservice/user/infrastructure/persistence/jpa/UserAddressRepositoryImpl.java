package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import com.trusta_market.userservice.user.application.port.out.UserAddressRepository;
import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// UserAddressRepository 인터페이스의 JPA 기반 구현체
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

}
