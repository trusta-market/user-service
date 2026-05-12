package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum ReportErrorCode implements ErrorCodeSpec {
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "신고 내역을 찾을 수 없습니다."),
    SELF_REPORT(HttpStatus.BAD_REQUEST, "R002", "본인을 신고할 수 없습니다."),
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "R003", "이미 신고된 내역입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ReportErrorCode(HttpStatus status, String code, String message) {
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

