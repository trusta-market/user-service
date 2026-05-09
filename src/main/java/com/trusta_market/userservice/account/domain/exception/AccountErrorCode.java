package com.trusta_market.userservice.account.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import org.springframework.http.HttpStatus;

public enum AccountErrorCode implements ErrorCodeSpec {
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "계좌를 찾을 수 없습니다."),
    ACCOUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "A002", "계좌는 최대 5개까지만 등록 가능합니다."),
    INVALID_BANK_CODE(HttpStatus.BAD_REQUEST, "A003", "올바르지 않은 은행 코드입니다."),
    INVALID_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST, "A004", "올바르지 않은 계좌 번호입니다."),
    INVALID_ACCOUNT_HOLDER(HttpStatus.BAD_REQUEST, "A005", "올바르지 않은 예금주명입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "A006", "잘못된 입력값입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AccountErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() { return status; }
    @Override
    public String getCode() { return code; }
    @Override
    public String getMessage() { return message; }
    @Override
    public String getField() { return null; }
}
