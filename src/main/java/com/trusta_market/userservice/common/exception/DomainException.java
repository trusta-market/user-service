package com.trusta_market.userservice.common.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;

public class DomainException extends CustomException {
    public DomainException(ErrorCodeSpec errorCode) {
        super(errorCode);
    }
}
