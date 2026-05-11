package com.trusta_market.userservice.common.pagination;

import java.util.List;
import java.util.function.Function;

public record DomainPage<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {
    public DomainPage {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page index cannot be less than zero");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("Total elements cannot be less than zero");
        }
    }

    public static <T> DomainPage<T> of(List<T> content, int page, int size, long totalElements) {
        return new DomainPage<>(content, page, size, totalElements);
    }

    public <U> DomainPage<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.content.stream()
                .<U>map(converter)
                .toList();
        return new DomainPage<>(convertedContent, page, size, totalElements);
    }
}
