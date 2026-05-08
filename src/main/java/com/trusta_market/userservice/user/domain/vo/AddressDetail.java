package com.trusta_market.userservice.user.domain.vo;

public record AddressDetail(String value) {
    public AddressDetail {
        value = value != null ? value.trim() : null;
        if (value != null && value.length() > 100) {
            throw new IllegalArgumentException("Address detail is too long");
        }
    }

    public static AddressDetail of(String value) {
        return new AddressDetail(value);
    }
}
