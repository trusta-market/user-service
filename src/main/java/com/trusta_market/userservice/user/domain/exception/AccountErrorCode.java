package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum AccountErrorCode implements ErrorCodeSpec {
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "AC01", "계좌 정보를 찾을 수 없습니다."),
    ACCOUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "AC02", "계좌 생성 한도(5개)를 초과했습니다."),
    ACCOUNT_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AC03", "인증되지 않은 계좌입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() { return status; }
    @Override
    public String getCode() { return code; }
    @Override
    public String getMessage() { return message; }
    @Override
    public String getField() { return null; }
}

