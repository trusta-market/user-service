package com.trusta_market.userservice.user.domain.vo;

public record Realname(String value) {

    private static final int MAX_LENGTH = 100;

    public Realname {
        if (value == null) {
            throw new IllegalArgumentException("realName?? ?熬곣뫖????낅퉵??");
        }
        String normalized = value.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("realName?? ?熬곣뫖????낅퉵??");
        }
        if (normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("realName?? %d????袁⑤┃??????紐껊퉵??".formatted(MAX_LENGTH));
        }
        value = normalized;
    }

    public static Realname of(String value) {
        return new Realname(value);
    }
}
