package com.trusta_market.userservice.user.domain.pagination;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

public record DomainPageRequest(int page, int size) {
    public DomainPageRequest {
        if (page < 0) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
        if (size < 1) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static DomainPageRequest of(int page, int size) {
        return new DomainPageRequest(page, size);
    }
}
