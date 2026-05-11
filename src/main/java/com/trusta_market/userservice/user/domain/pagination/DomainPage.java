package com.trusta_market.userservice.user.domain.pagination;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

import java.util.List;
import java.util.function.Function;

public record DomainPage<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public DomainPage {
        if (content == null) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
        if (page < 0 || size < 0 || totalElements < 0 || totalPages < 0) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
        content = List.copyOf(content);
    }
    public static <T> DomainPage<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        return new DomainPage<>(content, page, size, totalElements, totalPages);
    }

    public <R> DomainPage<R> map(Function<? super T, ? extends R> converter) {
        List<R> mappedContent = content.stream()
                .<R>map(converter)
                .toList();
        return new DomainPage<>(mappedContent, page, size, totalElements, totalPages);
    }
}
