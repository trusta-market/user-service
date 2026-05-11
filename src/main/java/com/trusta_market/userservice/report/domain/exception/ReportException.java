package com.trusta_market.userservice.report.domain.exception;

import lombok.Getter;

// 신고 도메인 전역에서 사용하는 예외 클래스 (프레임워크 독립적)
@Getter
public class ReportException extends RuntimeException {
    private final ReportErrorCode errorCode;

    public ReportException(ReportErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
