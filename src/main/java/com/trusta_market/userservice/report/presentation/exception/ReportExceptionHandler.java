package com.trusta_market.userservice.report.presentation.exception;

import com.trusta_market.userservice.report.domain.exception.ReportException;
import com.trustamarket.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 도메인 예외를 공통 응답 규격으로 변환하여 처리하는 전역 핸들러
@RestControllerAdvice
public class ReportExceptionHandler {

    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorResponse> handleReportException(ReportException e) {
        ReportErrorCodeAdapter adapter = new ReportErrorCodeAdapter(e.getErrorCode());
        
        // ErrorResponse.of()는 공통 모듈의 표준 팩토리 메서드라고 가정합니다.
        // 만약 실제 메서드명이 다르다면 공통 모듈 규격에 맞춰 수정이 필요할 수 있습니다.
        return ResponseEntity
                .status(adapter.getStatus())
                .body(ErrorResponse.of(adapter));
    }
}
