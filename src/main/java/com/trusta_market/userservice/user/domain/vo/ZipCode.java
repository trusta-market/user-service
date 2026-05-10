package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

import java.util.regex.Pattern;

public record ZipCode(String value) {
    private static final Pattern PATTERN = Pattern.compile("^\\d{5}$");

    public ZipCode {
        value = value != null ? value.trim() : null;
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new UserException(UserErrorCode.INVALID_ZIP_CODE);
        }
    }

    public static ZipCode of(String value) {
        return new ZipCode(value);
    }
}
