package com.trusta_market.userservice.common.pagination;

public record DomainPageRequest(int page, int size) {
    public static DomainPageRequest of(int page, int size) {
        return new DomainPageRequest(page, size);
    }
}
