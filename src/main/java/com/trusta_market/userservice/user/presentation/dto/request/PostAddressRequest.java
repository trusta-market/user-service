package com.trusta_market.userservice.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PostAddressRequest(
        @NotBlank String recipientName,
        @NotBlank String recipientPhone,
        @NotBlank String zipCode,
        @NotBlank String address,
        String addressDetail
) {
}
