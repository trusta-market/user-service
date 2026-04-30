package com.trusta_market.userservice.user.domain.pagination;

public record DomainPageRequest(int page, int size) {
    public DomainPageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
    }

    public static DomainPageRequest of(int page, int size) {
        return new DomainPageRequest(page, size);
    }
}
