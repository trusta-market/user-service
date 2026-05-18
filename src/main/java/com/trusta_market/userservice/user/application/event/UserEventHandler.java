package com.trusta_market.userservice.user.application.event;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.event.UserCreatedEvent;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTask;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final WalletPort walletPort;
    private final WalletCreationTaskRepository walletCreationTaskRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        UUID userId = event.userId().value();
        log.info("Starting wallet creation process for user: {}", userId);

        // PENDING 태스크 선저장 — 로컬 DB 무결성을 위해 내부 userId로 기록!
        WalletCreationTask task = walletCreationTaskRepository.findByUserId(userId)
                .orElseGet(() -> walletCreationTaskRepository.save(new WalletCreationTask(userId)));

        try {
            // 외부 지갑 서비스 연동 시에는 Keycloak ID 전달!
            boolean success = walletPort.createWallet(event.keycloakId());

            if (success) {
                task.complete();
                walletCreationTaskRepository.save(task);
                log.info("Wallet created successfully for user: {}", userId);
            } else {
                log.warn("Wallet creation failed for user: {}. Task remains PENDING for retry.", userId);
            }
        } catch (Exception e) {
            log.error("Error occurred while creating wallet for user: {}. Task remains PENDING for retry.", userId, e);
        }
    }
}
