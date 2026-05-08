package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseUserEntity;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_addresses")
public class UserAddress extends BaseUserEntity {

    @Id
    @Column(name = "address_id", nullable = false, updatable = false)
    private UUID addressId;
    
    @Column(nullable = false)
    private UserId userId;

    @Column(nullable = false, length = 50)
    private Name recipientName;

    @Column(nullable = false, length = 20)
    private PhoneNumber recipientPhone;

    @Column(nullable = false, length = 10)
    private ZipCode zipCode;

    @Column(nullable = false, length = 200)
    private AddressInfo address;

    @Column(length = 100)
    private AddressDetail addressDetail;

    @Column(nullable = false)
    private boolean isDefault;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public UserAddress(AddressId addressId, UserId userId, Name recipientName, PhoneNumber recipientPhone, ZipCode zipCode, AddressInfo address, AddressDetail addressDetail, boolean isDefault, Integer version) {
        if (addressId == null || userId == null || recipientName == null || recipientPhone == null || zipCode == null || address == null) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
        this.addressId = addressId.value();
        this.userId = userId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
        this.version = version;
    }

    public AddressId getAddressId() {
        return addressId != null ? AddressId.of(addressId) : null;
    }

    public static UserAddress create(UserId userId, Name recipientName, PhoneNumber recipientPhone, ZipCode zipCode, AddressInfo address, AddressDetail addressDetail, boolean isDefault) {
        return UserAddress.builder()
                .addressId(AddressId.of(UUID.randomUUID()))
                .userId(userId)
                .recipientName(recipientName)
                .recipientPhone(recipientPhone)
                .zipCode(zipCode)
                .address(address)
                .addressDetail(addressDetail)
                .isDefault(isDefault)
                .build();
    }

    public void update(Name recipientName, PhoneNumber recipientPhone, ZipCode zipCode, AddressInfo address, AddressDetail addressDetail) {
        if (recipientName != null) this.recipientName = recipientName;
        if (recipientPhone != null) this.recipientPhone = recipientPhone;
        if (zipCode != null) this.zipCode = zipCode;
        if (address != null) this.address = address;
        if (addressDetail != null) this.addressDetail = addressDetail;
    }

    public void markAsDefaultAddress() {
        this.isDefault = true;
    }

    public void unmarkDefaultAddress() {
        this.isDefault = false;
    }
}
