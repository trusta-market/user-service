package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record Name(String value) {
    public Name {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() < 2 || value.length() > 20) {
            throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }
}
