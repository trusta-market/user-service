package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCodeSpec {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "U003", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U004", "이미 사용 중인 이름입니다."),
    ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "U005", "이미 탈퇴한 회원입니다."),
    PENDING_USER(HttpStatus.FORBIDDEN, "U006", "승인 대기 중인 회원입니다."),
    REJECTED_USER(HttpStatus.FORBIDDEN, "U007", "가입 신청이 거절된 회원입니다."),
    SUSPENDED_USER(HttpStatus.FORBIDDEN, "U008", "정지된 회원입니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "U009", "올바르지 않은 이메일 형식입니다."),
    INVALID_NAME(HttpStatus.BAD_REQUEST, "U010", "올바르지 않은 이름 형식입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "U012", "올바르지 않은 입력값입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UserErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getField() {
        return null;
    }
}
