package com.trusta_market.userservice.report.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum ReportErrorCode implements ErrorCodeSpec {
    SELF_REPORT(HttpStatus.BAD_REQUEST, "R001", "자기 자신은 신고할 수 없습니다."),
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "R002", "이미 신고한 사용자입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "R003", "신고 내역을 찾을 수 없습니다.");

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
