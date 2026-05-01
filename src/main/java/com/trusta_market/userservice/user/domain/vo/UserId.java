package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if (value == null) {
            throw new DomainException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
