package com.trusta_market.userservice.user.domain.vo;

public record Nickname(String value) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    public Nickname {
        if (value == null) {
            throw new IllegalArgumentException("nickname?? ?熬곣뫖????낅퉵??");
        }
        String normalized = value.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("nickname?? ?熬곣뫖????낅퉵??");
        }
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("nickname?? %d????怨대쭜 %d????袁⑤┃??????紐껊퉵??".formatted(MIN_LENGTH, MAX_LENGTH));
        }
        value = normalized;
    }

    public static Nickname of(String value) {
        return new Nickname(value);
    }
}
