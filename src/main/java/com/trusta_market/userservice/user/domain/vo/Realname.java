package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record Realname(String value) {

    private static final int MAX_LENGTH = 100;

    public Realname {
        if (value == null || value.trim().isBlank()) {
            throw new UserException(UserErrorCode.INVALID_REALNAME_FORMAT);
        }
        String normalized = value.trim();
        if (normalized.length() > MAX_LENGTH) {
            throw new UserException(UserErrorCode.INVALID_REALNAME_FORMAT);
        }
        value = normalized;
    }

    public static Realname of(String value) {
        return new Realname(value);
    }
}
