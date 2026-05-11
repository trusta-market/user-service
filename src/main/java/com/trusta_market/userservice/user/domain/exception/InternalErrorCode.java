package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum InternalErrorCode implements ErrorCodeSpec {
    USER_NOT_ACTIVE(HttpStatus.FORBIDDEN, "U001", "활성화된 사용자가 아닙니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    InternalErrorCode(HttpStatus status, String code, String message) {
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
