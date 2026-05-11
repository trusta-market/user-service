package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

import java.util.regex.Pattern;

public record PhoneNumber(String value) {
    private static final Pattern PATTERN = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}$");

    public PhoneNumber {
        value = value != null ? value.trim() : null;
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new UserException(UserErrorCode.INVALID_PHONE_FORMAT);
        }
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
