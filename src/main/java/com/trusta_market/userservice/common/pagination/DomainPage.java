package com.trusta_market.userservice.common.pagination;

import lombok.Getter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class DomainPage<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;

    public DomainPage(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }

    public static <T> DomainPage<T> of(List<T> content, int page, int size, long totalElements) {
        return new DomainPage<>(content, page, size, totalElements);
    }

    public <U> DomainPage<U> map(Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.content.stream().map(converter).collect(Collectors.toList());
        return new DomainPage<>(convertedContent, page, size, totalElements);
    }
}
