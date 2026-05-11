package com.trusta_market.userservice.user.application.dto.result;

import com.trusta_market.userservice.user.domain.entity.UserAddress;
import java.util.UUID;

// 배송지 결과 데이터 객체
public record AddressResult(
    UUID addressId,
    UUID userId,
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail,
    boolean isDefault
) {
    // 엔티티로부터 Result 객체 생성
    public static AddressResult from(UserAddress address) {
        return new AddressResult(
            address.getAddressId().value(),
            address.getUserId().value(),
            address.getRecipientName() != null ? address.getRecipientName().value() : null,
            address.getRecipientPhone() != null ? address.getRecipientPhone().value() : null,
            address.getZipCode() != null ? address.getZipCode().value() : null,
            address.getAddress() != null ? address.getAddress().value() : null,
            address.getAddressDetail() != null ? address.getAddressDetail().value() : null,
            address.isDefault()
        );
    }
}
