package com.trusta_market.userservice.membership.infrastructure.kafka;

import com.trusta_market.userservice.membership.application.port.in.MembershipPointUseCase;
import com.trusta_market.userservice.membership.infrastructure.kafka.dto.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConfirmedEventConsumer {

    private final MembershipPointUseCase membershipPointUseCase;

    @KafkaListener(
            topics = "${kafka.topic.order-confirmed:order.confirmed}",
            containerFactory = "orderConfirmedListenerContainerFactory"
    )
    public void consume(OrderConfirmedEvent event, Acknowledgment acknowledgment) {
        log.info("[Membership] OrderConfirmedEvent 수신 - orderId={}, buyerId={}, sellerId={}, amount={}",
                event.orderId(), event.buyerId(), event.sellerId(), event.amount());

        try {
            membershipPointUseCase.processOrderConfirmed(
                    event.orderId(),
                    event.buyerId(),
                    event.sellerId(),
                    event.amount()
            );
            // 처리 완료 후 명시적 오프셋 커밋
            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("[Membership] 포인트 지급 실패 - orderId={}, error={}", event.orderId(), e.getMessage(), e);
            // acknowledge 호출하지 않음 → 재시도 (오프셋 커밋 안 됨)
            throw e;
        }
    }
}
