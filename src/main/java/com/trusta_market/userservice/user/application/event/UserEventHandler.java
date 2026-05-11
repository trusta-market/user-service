package com.trusta_market.userservice.user.application.event;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 유저 도메인 이벤트 핸들러
 * 유저 생성 후 지갑 생성 등 비동기/분리된 처리를 담당합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final WalletPort walletPort;

    /**
     * 유저 생성 이벤트를 구독하여 지갑 생성을 요청합니다.
     * TransactionPhase.AFTER_COMMIT을 사용하여 유저 정보가 DB에 최종 반영된 후 호출합니다.
     *
     * @param event 유저 생성 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handling UserCreatedEvent for user: {}", event.userId().value());
        try {
            walletPort.createWallet(event.userId());
        } catch (Exception e) {
            log.error("Failed to process UserCreatedEvent for wallet creation. userId: {}", event.userId().value(), e);
            // 필요 시 재시도 로직이나 보상 트랜잭션 등을 고려할 수 있습니다.
        }
    }
}
