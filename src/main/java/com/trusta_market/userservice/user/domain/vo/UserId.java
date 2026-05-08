package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if (value == null) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
