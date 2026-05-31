package com.trusta_market.userservice.membership.infrastructure.kafka.dto;

import java.util.UUID;

/**
 * Order Service에서 발행하는 주문 확정 이벤트.
 * 추후 common 라이브러리로 이동 예정.
 */
public record OrderConfirmedEvent(
        UUID orderId,
        UUID buyerId,
        UUID sellerId,
        long amount
) {}
