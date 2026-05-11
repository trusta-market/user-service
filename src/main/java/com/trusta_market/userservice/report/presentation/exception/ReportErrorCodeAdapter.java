package com.trusta_market.userservice.report.presentation.exception;

import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;
import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// ReportErrorCode를 공통 ErrorCodeSpec으로 변환하는 어댑터
@Getter
public class ReportErrorCodeAdapter implements ErrorCodeSpec {

    private final HttpStatus status;
    private final String code;
    private final String message;

    private ReportErrorCodeAdapter(ReportErrorCode errorCode) {
        this.status = HttpStatus.valueOf(errorCode.getStatus());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ReportErrorCodeAdapter of(ReportErrorCode errorCode) {
        return new ReportErrorCodeAdapter(errorCode);
    }

    @Override
    public String getField() {
        return null;
    }
}
