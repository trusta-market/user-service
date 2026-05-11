package com.trusta_market.userservice.account.domain.vo;

import lombok.Getter;

/**
 * 계좌의 인증 상태를 나타내는 Enum
 */
@Getter
public enum AccountStatus {
    PENDING("인증 대기"),
    VERIFIED("인증 완료"),
    FAILED("인증 실패");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }
}
