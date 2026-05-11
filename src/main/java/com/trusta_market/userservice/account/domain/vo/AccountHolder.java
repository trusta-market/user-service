package com.trusta_market.userservice.account.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record AccountHolder(String value) {
    public AccountHolder {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_ACCOUNT_HOLDER);
        }
    }
    
    public static AccountHolder of(String value) {
        return new AccountHolder(value);
    }
}
