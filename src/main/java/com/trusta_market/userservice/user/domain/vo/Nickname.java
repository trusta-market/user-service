package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record Nickname(String value) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    public Nickname {
        if (value == null || value.trim().isBlank()) {
            throw new UserException(UserErrorCode.INVALID_NICKNAME_FORMAT);
        }
        String normalized = value.trim();
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new UserException(UserErrorCode.INVALID_NICKNAME_FORMAT);
        }
        value = normalized;
    }

    public static Nickname of(String value) {
        return new Nickname(value);
    }
}
