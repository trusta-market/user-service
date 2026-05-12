package com.trusta_market.userservice.user.domain.exception;

import lombok.Getter;

import java.util.Objects;

// 유저 도메인 전역에서 사용하는 예외 클래스 (프레임워크 독립적)
@Getter
public class UserException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
        super(Objects.requireNonNull(errorCode, "UserErrorCode must not be null").getMessage());
        this.errorCode = errorCode;
    }

    public UserException(UserErrorCode errorCode, Throwable cause) {
        super(Objects.requireNonNull(errorCode, "UserErrorCode must not be null").getMessage(), cause);
        this.errorCode = errorCode;
    }
}
