package com.trusta_market.userservice.user.domain.vo;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null) {
            throw new IllegalArgumentException("email?? ?熬곣뫖????낅퉵??");
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("email?? ?熬곣뫖????낅퉵??");
        }
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("email ?筌먦끇六??????紐?? ???용????덈펲.");
        }
        value = normalized;
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
