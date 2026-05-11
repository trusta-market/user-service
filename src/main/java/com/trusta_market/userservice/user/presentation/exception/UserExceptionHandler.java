package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trustamarket.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 유저 도메인 통합 예외 핸들러
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<CommonResponse<Void>> handleUserException(UserException e) {
        log.error("User Domain Exception: {}", e.getMessage());
        UserErrorCodeAdapter adapter = UserErrorCodeAdapter.of(e.getErrorCode());
        return ResponseEntity
                .status(adapter.getStatus())
                .body(CommonResponse.error(adapter));
    }
}
