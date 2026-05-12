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

    /**
     * 매 1분마다 지갑 생성에 실패했던 PENDING 상태의 작업들을 재시도합니다.
     */
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
                    task.retry(); // 카운트 증가 및 PENDING 유지
                    log.warn("Retry failed for user: {}. Retry count: {}. Will try again later.", task.getUserId(), task.getRetryCount());
                }
            } catch (Exception e) {
                task.retry(); // 예외 발생 시에도 카운트 증가 및 PENDING 유지
                log.error("Error during retry for user: {}. Error: {}. Retry count: {}", task.getUserId(), e.getMessage(), task.getRetryCount());
            }
            walletCreationTaskRepository.save(task); // 변경사항 DB 반영
        }
    }
}
