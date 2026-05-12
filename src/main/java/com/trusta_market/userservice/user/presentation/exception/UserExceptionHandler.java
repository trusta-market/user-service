package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trustamarket.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// 유저 도메인 통합 예외 핸들러 (도메인 에러 코드가 직접 ErrorCodeSpec을 구현하므로 별도의 어댑터 없이 처리)
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    // 도메인 예외(UserException) 발생 시 에러 코드 규격에 맞춰 상세 내용 반환
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

    // Bean Validation(@Valid) 실패 시 발생하는 예외를 가로채 첫 번째 에러 메시지 반환
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("Validation Error: {}", message);
        return ResponseEntity.badRequest().body(ErrorResponse.of(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "V001",
                message,
                e.getBindingResult().getFieldError() != null ? e.getBindingResult().getFieldError().getField() : null
        ));
    }

    // 컨트롤러 메서드 인자의 타입이 일치하지 않을 경우(UUID 형식 오류 등) 에러 반환
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = String.format("Parameter '%s' has invalid value '%s'. Expected type: %s",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        log.error("Type Mismatch Error: {}", message);
        return ResponseEntity.badRequest().body(ErrorResponse.of(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "V002",
                message,
                e.getName()
        ));
    }
}
