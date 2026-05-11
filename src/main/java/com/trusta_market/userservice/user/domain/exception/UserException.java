package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;

public class UserException extends CustomException {
    private final ErrorCodeSpec errorCode;

    public UserException(ErrorCodeSpec errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public ErrorCodeSpec getErrorCode() {
        return errorCode;
    }
}
