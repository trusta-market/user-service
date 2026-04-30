package com.trusta_market.userservice.user.domain.repository;

import com.trusta_market.userservice.user.domain.entity.UserAddress;

import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository {
    UserAddress save(UserAddress address);

    Optional<UserAddress> findById(AddressId addressId);

    List<UserAddress> findAllActiveAddressesByUserId(UserId userId);

    long countActiveAddressesByUserId(UserId userId);

    Optional<UserAddress> findActiveAddressByIdAndUserId(AddressId addressId, UserId userId);

    Optional<UserAddress> findDefaultAddressByUserId(UserId userId);

    void clearDefaultAddress(UserId userId);
}
