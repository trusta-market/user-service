package com.trusta_market.userservice.account.domain.vo;

import com.trusta_market.userservice.account.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.account.domain.exception.AccountException;


public record AccountHolder(String value) {
    public AccountHolder {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() > 50) {
            throw new AccountException(AccountErrorCode.INVALID_ACCOUNT_HOLDER);
        }
    }
    
    public static AccountHolder of(String value) {
        return new AccountHolder(value);
    }
}
