package com.trusta_market.userservice.trustscore.domain.vo;

public record Score(long value) {
    public Score {
        if (value < 0) {
            value = 0;
        }
    }

    public static Score of(long value) {
        return new Score(value);
    }

    public Score add(long delta) {
        return new Score(this.value + delta);
    }
}
