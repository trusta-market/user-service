package com.trusta_market.userservice.user.application.dto.command;

import java.util.UUID;

// 배송지 생성 명령 객체
public record CreateAddressCommand(
    UUID userId,
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail
) {
}
