package com.trusta_market.userservice.trustscore.domain.vo;

public record Score(long value) {
    public Score {
        if (value < 0) {
            // 신뢰 점수는 0점 미만으로 내려가지 않는다고 가정 (정책에 따라 변경 가능)
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
