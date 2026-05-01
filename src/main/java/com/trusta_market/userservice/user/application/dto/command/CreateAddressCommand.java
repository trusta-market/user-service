package com.trusta_market.userservice.user.application.dto.command;

import java.util.UUID;

public record CreateAddressCommand(
        UUID userId,
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail
) {
}
