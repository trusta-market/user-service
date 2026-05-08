package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;

public class UserException extends CustomException {
    public UserException(ErrorCodeSpec errorCode) {
        super(errorCode);
    }
}
