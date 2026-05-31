package com.trusta_market.userservice.membership.domain;

/**
 * 거래 금액에 따른 멤버십 포인트 계산 도메인 로직.
 * 구간 전체에 단일 요율 적용 (누진 아님), 소수점 버림.
 *
 * <pre>
 *   1원 ~  49,999원 : 1,000원당 1점
 *  50,000 ~  99,999원 : 1,000원당 2점
 * 100,000 ~ 499,999원 : 1,000원당 3점
 * 500,000원 이상       : 1,000원당 4점
 * </pre>
 */
public class MembershipPointCalculator {

    private MembershipPointCalculator() {}

    public static int calculate(long amount) {
        int rate;
        if      (amount < 50_000L)  rate = 1;
        else if (amount < 100_000L) rate = 2;
        else if (amount < 500_000L) rate = 3;
        else                        rate = 4;

        return (int)(amount / 1_000) * rate;
    }
}
