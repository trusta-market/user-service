package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Wallet 서비스 장애 시 실행될 대체 로직 (Fallback)
@Slf4j
@Component
public class WalletFeignClientFallback implements WalletFeignClient {

    @Override
    public WalletCreateResponse createWallet(WalletCreateRequest request) {
        log.error("Wallet service is unavailable or circuit breaker opened for user: {}", request.userId());
        return new WalletCreateResponse(false);
    }
}
