package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTask;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity.WalletCreationTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Wallet 서비스 장애 시 실행될 대체 로직 (Fallback)
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletFeignClientFallback implements WalletFeignClient {

    private final WalletCreationTaskRepository walletCreationTaskRepository;

    @Override
    public boolean createWallet(WalletCreateRequest request) {
        log.error("Wallet service is unavailable. Saving pending task for user: {}", request.userId());
        
        try {
            java.util.UUID userId = request.userId();
            if (walletCreationTaskRepository.findByUserId(userId).isEmpty()) {
                walletCreationTaskRepository.save(new WalletCreationTask(userId));
                log.info("Successfully saved pending wallet creation task for user: {}", userId);
            }
        } catch (Exception e) {
            log.error("Failed to save pending wallet creation task for user: {}", request.userId(), e);
        }
        return false;
    }
}
