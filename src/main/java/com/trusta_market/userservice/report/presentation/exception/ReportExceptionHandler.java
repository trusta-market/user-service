package com.trusta_market.userservice.report.presentation.exception;

import com.trusta_market.userservice.report.domain.exception.ReportException;
import com.trustamarket.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 신고 도메인 예외 핸들러
 * 도메인 에러 코드가 직접 ErrorCodeSpec을 구현하므로 별도의 어댑터 없이 처리합니다.
 */
@Slf4j
@RestControllerAdvice
public class ReportExceptionHandler {

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorResponse> handleReportException(ReportException e) {
        log.error("Report Domain Exception: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(
                        e.getErrorCode().getStatus(),
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage(),
                        e.getErrorCode().getField()
                ));
    }
}
