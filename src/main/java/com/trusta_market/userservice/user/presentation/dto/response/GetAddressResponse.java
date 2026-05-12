package com.trusta_market.userservice.user.presentation.dto.response;

import com.trusta_market.userservice.user.application.dto.result.AddressResult;
import java.util.UUID;

// 배송지 정보 응답 객체
public record GetAddressResponse(
    UUID addressId,
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail,
    boolean isDefault
) {
    // Result 객체로부터 Response 생성
    public static GetAddressResponse from(AddressResult result) {
        return new GetAddressResponse(
            result.addressId(),
            result.recipientName(),
            result.recipientPhone(),
            result.zipCode(),
            result.address(),
            result.addressDetail(),
            result.isDefault()
        );
    }
}
