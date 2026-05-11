package com.trusta_market.userservice.account.domain.vo;

import com.trusta_market.userservice.account.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.account.domain.exception.AccountException;

public record BankCode(String value) {
    public BankCode {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() > 10) {
            throw new AccountException(AccountErrorCode.INVALID_BANK_CODE);
        }
    }

    public static BankCode of(String value) {
        return new BankCode(value);
    }
}
