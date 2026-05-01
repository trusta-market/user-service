package com.trusta_market.userservice.user.presentation.dto.response;

import com.trusta_market.userservice.user.application.dto.result.AddressResult;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetAddressResponse(
        UUID addressId,
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail,
        boolean isDefault,
        LocalDateTime createdAt
) {
    public static GetAddressResponse from(AddressResult result) {
        return new GetAddressResponse(
                result.addressId(),
                result.recipientName(),
                result.recipientPhone(),
                result.zipCode(),
                result.address(),
                result.addressDetail(),
                result.isDefault(),
                result.createdAt()
        );
    }
}
