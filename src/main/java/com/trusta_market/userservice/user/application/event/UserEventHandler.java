package com.trusta_market.userservice.user.application.event;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.event.UserCreatedEvent;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTask;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTaskRepository;
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
    private final WalletCreationTaskRepository walletCreationTaskRepository;

    /**
     * 유저 생성 이벤트를 구독하여 지갑 생성을 처리합니다. (Outbox 패턴 적용)
     * 1. DB에 PENDING 상태로 작업 기록 저장
     * 2. 비동기로 지갑 서비스 호출 시도
     * 3. 성공 시 COMPLETED 상태로 업데이트
     */
    @org.springframework.scheduling.annotation.Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Starting wallet creation process for user: {}", event.userId().value());
        
        java.util.UUID userId = event.userId().value();
        
        // 1. 먼저 DB에 PENDING 상태로 저장 (나중에 스케줄러가 챙길 수 있도록)
        WalletCreationTask task = walletCreationTaskRepository.findByUserId(userId)
                .orElseGet(() -> walletCreationTaskRepository.save(new WalletCreationTask(userId)));

        try {
            // 2. 지갑 서비스 호출 시도
            boolean success = walletPort.createWallet(event.userId());
            
            // 3. 성공 시 완료 처리
            if (success) {
                task.complete();
                walletCreationTaskRepository.save(task);
                log.info("Wallet created successfully for user: {}", userId);
            } else {
                log.warn("Wallet creation failed or fallback triggered for user: {}. Task remains PENDING.", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while creating wallet for user: {}. Task remains PENDING.", userId, e);
        }
    }
}
