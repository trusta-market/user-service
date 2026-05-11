package com.trusta_market.userservice.user.presentation.dto.request;

public record PatchAddressRequest(
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail
) {
}
