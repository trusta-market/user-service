package com.trusta_market.userservice.user.application.dto.command;

// 배송지 수정 명령 객체
public record UpdateAddressCommand(
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail
) {
    public UpdateAddressCommand {
        // 수정 명령에서는 필드들이 선택적일 수 있으나, 최소한 하나의 필드는 있어야 하거나 
        // 전달된 값이 있는 경우 유효해야 합니다. 여기서는 단순 null 체크보다는 
        // 전체가 null인 경우 등을 체크하거나 개별 필드 제약을 적용합니다.
        // 프로젝트 가이드에 따라 필수 필드가 있다면 체크합니다.
    }
}
