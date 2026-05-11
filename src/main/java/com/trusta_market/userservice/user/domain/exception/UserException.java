package com.trusta_market.userservice.user.domain.exception;

import lombok.Getter;

// 유저 도메인 전역에서 사용하는 예외 클래스 (프레임워크 독립적)
@Getter
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
