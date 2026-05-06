package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record KeycloakId(String value) {
    public KeycloakId {
        if (value == null || value.trim().isBlank()) {
            throw new DomainException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static KeycloakId of(String value) {
        return new KeycloakId(value);
    }
}
