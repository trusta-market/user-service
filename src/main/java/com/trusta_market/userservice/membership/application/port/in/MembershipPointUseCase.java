package com.trusta_market.userservice.membership.application.port.in;

import java.util.UUID;

public interface MembershipPointUseCase {

    /**
     * 거래 확정 시 buyer / seller 양쪽에 포인트를 지급하고 등급을 재계산합니다.
     *
     * @param orderId  거래 식별자 (멱등성 보장에 사용)
     * @param buyerId  구매자 userId
     * @param sellerId 판매자 userId
     * @param amount   결제 금액
     */
    void processOrderConfirmed(UUID orderId, UUID buyerId, UUID sellerId, long amount);
}
