package com.trusta_market.userservice.user.application.dto.command;

// 배송지 수정 명령 객체
public record UpdateAddressCommand(
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail
) {
}
