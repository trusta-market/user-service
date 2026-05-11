package com.trusta_market.userservice.account.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record AccountNumber(String value) {
    public AccountNumber {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_ACCOUNT_NUMBER);
        }
    }
    
    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }
}
