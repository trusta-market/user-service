package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trustamarket.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 유저 도메인 전용 예외 핸들러
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    // 도메인 예외(UserException) 발생 시 에러 코드 규격에 맞춰 상세 내용 반환
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        log.error("User Domain Exception: {}", e.getMessage());
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
