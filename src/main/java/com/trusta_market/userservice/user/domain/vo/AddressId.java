package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

import java.util.UUID;

public record AddressId(UUID value) {
    public AddressId {
        if (value == null) {
            throw new DomainException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static AddressId of(UUID value) {
        return new AddressId(value);
    }
}
