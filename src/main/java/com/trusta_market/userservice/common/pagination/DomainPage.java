package com.trusta_market.userservice.common.pagination;

import java.util.List;
import java.util.function.Function;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.common.exception.CommonErrorCode;

public record DomainPage<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {
    public DomainPage {
        if (content == null) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
        if (page < 0) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
        if (size <= 0) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
        if (totalElements < 0) {
            throw new DomainException(CommonErrorCode.INVALID_INPUT);
        }
    }

    public static <T> DomainPage<T> of(List<T> content, int page, int size, long totalElements) {
        return new DomainPage<>(content, page, size, totalElements);
    }

    public <U> DomainPage<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.content.stream().map(converter).map(u -> (U) u).toList();
        return new DomainPage<>(convertedContent, page, size, totalElements);
    }
}
