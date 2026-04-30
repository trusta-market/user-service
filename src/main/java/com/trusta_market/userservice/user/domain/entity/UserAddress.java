package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseUserEntity;
import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.AddressIdConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.UserIdConverter;
import jakarta.persistence.Convert;
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
    @Convert(converter = AddressIdConverter.class)
    @Column(nullable = false, updatable = false)
    private AddressId addressId;

    @Convert(converter = UserIdConverter.class)
    @Column(nullable = false)
    private UserId userId;

    @Column(nullable = false, length = 50)
    private String recipientName;

    @Column(nullable = false, length = 20)
    private String recipientPhone;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(length = 100)
    private String addressDetail;

    @Column(nullable = false)
    private boolean isDefault;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public UserAddress(AddressId addressId, UserId userId, String recipientName, String recipientPhone, String zipCode, String address, String addressDetail, boolean isDefault, Integer version) {
        this.addressId = addressId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
        this.version = version;
    }

    public static UserAddress create(UserId userId, String recipientName, String recipientPhone, String zipCode, String address, String addressDetail, boolean isDefault) {
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

    public void update(String recipientName, String recipientPhone, String zipCode, String address, String addressDetail) {
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
