package com.trusta_market.userservice.user.application.event;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.event.UserCreatedEvent;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTask;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Starting wallet creation process for user: {}", event.userId().value());

        UUID userId = event.userId().value();

        // PENDING 태스크 선저장 — 이후 스케줄러가 재시도 가능하도록
        WalletCreationTask task = walletCreationTaskRepository.findByUserId(userId)
                .orElseGet(() -> walletCreationTaskRepository.save(new WalletCreationTask(userId)));

        try {
            boolean success = walletPort.createWallet(event.userId());

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
