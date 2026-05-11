package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum AddressErrorCode implements ErrorCodeSpec {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "배송지 정보를 찾을 수 없습니다."),
    ADDRESS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "A002", "배송지 생성 한도(10개)를 초과했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AddressErrorCode(HttpStatus status, String code, String message) {
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

