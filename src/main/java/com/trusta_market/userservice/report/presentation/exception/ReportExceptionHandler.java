package com.trusta_market.userservice.report.presentation.exception;

import com.trusta_market.userservice.report.domain.exception.ReportException;
import com.trustamarket.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 신고 도메인 예외 핸들러
@Slf4j
@RestControllerAdvice
public class ReportExceptionHandler {

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<CommonResponse<Void>> handleReportException(ReportException e) {
        log.error("Report Domain Exception: {}", e.getMessage());
        ReportErrorCodeAdapter adapter = ReportErrorCodeAdapter.of(e.getErrorCode());
        return ResponseEntity
                .status(adapter.getStatus())
                .body(CommonResponse.error(adapter));
    }
}
