package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trustamarket.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 유저 도메인 통합 예외 핸들러
 * 도메인 에러 코드가 직접 ErrorCodeSpec을 구현하므로 별도의 어댑터 없이 처리합니다.
 */
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        log.error("User Domain Exception: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }
}
