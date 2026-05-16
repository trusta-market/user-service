package com.trusta_market.userservice.user.application.scheduler;

import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTask;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletRetryScheduler {

    private final WalletCreationTaskRepository walletCreationTaskRepository;
    private final WalletPort walletPort;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void retryWalletCreation() {
        List<WalletCreationTask> pendingTasks = walletCreationTaskRepository.findAllByStatus(WalletCreationTask.TaskStatus.PENDING);

        if (pendingTasks.isEmpty()) {
            return;
        }

        log.info("Starting retry for {} pending wallet creation tasks...", pendingTasks.size());

        for (WalletCreationTask task : pendingTasks) {
            try {
                log.info("Retrying wallet creation for user: {}", task.getUserId());
                boolean success = walletPort.createWallet(new UserId(task.getUserId()));

                if (success) {
                    task.complete();
                    log.info("Successfully completed wallet creation task for user: {}", task.getUserId());
                } else {
                    handleRetryFailure(task, "Wallet service returned failure");
                }
            } catch (Exception e) {
                log.error("Error during retry for user: {}", task.getUserId(), e);
                handleRetryFailure(task, e.getMessage());
            }
            walletCreationTaskRepository.save(task);
        }
    }

    private void handleRetryFailure(WalletCreationTask task, String errorMessage) {
        task.retry();
        if (task.isMaxRetriesExceeded()) {
            task.fail(errorMessage);
            log.error("Wallet creation permanently failed for user: {}. Retry count: {}. Moved to FAILED.",
                    task.getUserId(), task.getRetryCount());
        } else {
            log.warn("Retry failed for user: {}. Retry count: {}",
                    task.getUserId(), task.getRetryCount());

        }
    }
}
