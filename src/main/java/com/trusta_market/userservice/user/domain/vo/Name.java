package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import java.util.regex.Pattern;

public record Name(String value) {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣\\s]+$");

    public Name {
        value = value != null ? value.trim() : null;
        if (value == null || value.isEmpty() || value.length() < 2 || value.length() > 20 || !NAME_PATTERN.matcher(value).matches()) {
            throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }
}
