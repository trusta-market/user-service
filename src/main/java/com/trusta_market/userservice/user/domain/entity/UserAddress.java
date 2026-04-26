package com.trusta_market.userservice.user.domain.entity;

import com.trusta_market.userservice.common.domain.entity.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_addresses")
public class UserAddress extends BaseUserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID addressId;

    @Column(nullable = false)
    private UUID userId;

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

    @Builder
    public UserAddress(UUID addressId, UUID userId, String recipientName, String recipientPhone, String zipCode, String address, String addressDetail, boolean isDefault) {
        this.addressId = addressId;
        this.userId = userId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.zipCode = zipCode;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
    }

    public static UserAddress create(UUID userId, String recipientName, String recipientPhone, String zipCode, String address, String addressDetail, boolean isDefault) {
        return UserAddress.builder()
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

    public void setDefault(boolean value) {
        this.isDefault = value;
    }
}
