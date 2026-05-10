package com.trusta_market.userservice.account.domain.vo;

import com.trusta_market.userservice.account.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.account.domain.exception.AccountException;

public record AccountNumber(String value) {
    public AccountNumber {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty()) {
            throw new AccountException(AccountErrorCode.INVALID_ACCOUNT_NUMBER);
        }
    }
    
    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }
}
