package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

public record AddressInfo(String value) {
    public AddressInfo {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() > 200) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static AddressInfo of(String value) {
        return new AddressInfo(value);
    }
}
