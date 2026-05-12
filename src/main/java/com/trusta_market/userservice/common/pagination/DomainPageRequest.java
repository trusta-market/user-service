package com.trusta_market.userservice.common.pagination;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.common.exception.CommonErrorCode;

public record DomainPageRequest(int page, int size) {
    public DomainPageRequest {
        if (page < 0) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
        if (size <= 0) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
    }

    public static DomainPageRequest of(int page, int size) {
        return new DomainPageRequest(page, size);
    }
}
