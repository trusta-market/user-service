package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import org.springframework.util.StringUtils;

public record Name(String value) {
    public Name {
        if (!StringUtils.hasText(value) || value.length() < 2 || value.length() > 20) {
            throw new DomainException(UserErrorCode.INVALID_NAME);
        }
    }

    public static Name of(String value) {
        return new Name(value);
    }
}
