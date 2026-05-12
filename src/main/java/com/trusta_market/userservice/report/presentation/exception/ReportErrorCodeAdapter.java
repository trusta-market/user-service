package com.trusta_market.userservice.report.presentation.exception;

import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;
import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 도메인 에러 코드를 공통 모듈의 ErrorCodeSpec으로 변환하는 어댑터
@RequiredArgsConstructor
public class ReportErrorCodeAdapter implements ErrorCodeSpec {

    private final ReportErrorCode errorCode;

    @Override
    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getField() {
        return errorCode.getField();
    }
}
