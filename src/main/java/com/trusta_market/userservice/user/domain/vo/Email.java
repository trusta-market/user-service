package com.trusta_market.userservice.user.domain.vo;

import java.util.Locale;
import java.util.regex.Pattern;

import com.trusta_market.userservice.user.domain.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || value.trim().isBlank()) {
            throw new DomainException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new DomainException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
        value = normalized;
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
