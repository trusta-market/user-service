package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.CustomException;
import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.Getter;

@Getter
public class DomainException extends CustomException {

    public DomainException(ErrorCodeSpec errorCode) {
        super(errorCode);
    }
}

