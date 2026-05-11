package com.trusta_market.userservice.account.domain.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;

public class AccountException extends CustomException {
    public AccountException(ErrorCodeSpec errorCode) {
        super(errorCode);
    }
}
