package com.trusta_market.userservice.user.application.dto.result;

import com.trusta_market.userservice.user.domain.entity.UserAddress;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddressResult(
        UUID addressId,
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail,
        boolean isDefault,
        LocalDateTime createdAt
) {
    public static AddressResult from(UserAddress address) {
        return new AddressResult(
                address.getAddressId().value(),
                address.getRecipientName(),
                address.getRecipientPhone(),
                address.getZipCode(),
                address.getAddress(),
                address.getAddressDetail(),
                address.isDefault(),
                address.getCreatedAt() != null ? LocalDateTime.ofInstant(address.getCreatedAt(), java.time.ZoneId.systemDefault()) : null
        );
    }
}
