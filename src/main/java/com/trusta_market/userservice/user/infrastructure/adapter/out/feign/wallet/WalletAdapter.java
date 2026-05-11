package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WalletPort 인터페이스의 FeignClient 기반 구현체 (Adapter)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletAdapter implements WalletPort {

    private final WalletFeignClient walletFeignClient;

    @Override
    public void createWallet(UserId userId) {
        log.info("Requesting wallet creation for user: {}", userId.value());
        try {
            WalletCreateRequest request = new WalletCreateRequest(userId.value());
            walletFeignClient.createWallet(request);
            log.info("Successfully requested wallet creation for user: {}", userId.value());
        } catch (Exception e) {
            log.error("Failed to request wallet creation for user: {}", userId.value(), e);
            // 비즈니스 요구사항에 따라 예외를 다시 던지거나, 
            // 보상 트랜잭션/이벤트를 발행할 수 있습니다.
        }
    }
}
