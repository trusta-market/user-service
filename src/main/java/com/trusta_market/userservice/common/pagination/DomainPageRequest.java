package com.trusta_market.userservice.common.pagination;

public record DomainPageRequest(int page, int size) {
    public DomainPageRequest {
        if (page < 0) {
            throw new IllegalArgumentException("Page index cannot be less than zero");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
    }

    public static DomainPageRequest of(int page, int size) {
        return new DomainPageRequest(page, size);
    }
}
