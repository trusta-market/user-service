package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

public record AddressDetail(String value) {
    public AddressDetail {
        value = value != null ? value.trim() : null;
        if (value != null && value.length() > 100) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static AddressDetail of(String value) {
        return new AddressDetail(value);
    }
}
