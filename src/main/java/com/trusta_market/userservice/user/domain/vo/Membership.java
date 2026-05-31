package com.trusta_market.userservice.user.domain.vo;

public enum Membership {

    BRONZE  (0,    0.05),
    SILVER  (150,  0.04),
    GOLD    (500,  0.03),
    PLATINUM(1000, 0.02),
    DIAMOND (3000, 0.01);

    private final int requiredPoints;
    private final double feeRate;

    Membership(int requiredPoints, double feeRate) {
        this.requiredPoints = requiredPoints;
        this.feeRate = feeRate;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public double getFeeRate() {
        return feeRate;
    }

    /**
     * 최근 3개월 롤링 포인트 합산값으로 등급을 판정합니다.
     * 높은 등급부터 내려오면서 조건을 만족하는 첫 번째 등급을 반환합니다.
     */
    public static Membership of(int rollingPoints) {
        Membership[] values = Membership.values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (rollingPoints >= values[i].requiredPoints) {
                return values[i];
            }
        }
        return BRONZE;
    }
}
