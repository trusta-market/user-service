package com.trusta_market.userservice.user.application.dto.command;

public record UpdateAddressCommand(
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail
) {
}
