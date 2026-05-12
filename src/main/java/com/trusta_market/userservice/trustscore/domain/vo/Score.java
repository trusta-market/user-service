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
        try {
            return new Score(Math.addExact(this.value, delta));
        } catch (ArithmeticException e) {
            // 오버플로우 발생 시 도메인 예외로 전환
            throw new com.trusta_market.userservice.user.domain.exception.UserException(
                com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        }
    }
}
