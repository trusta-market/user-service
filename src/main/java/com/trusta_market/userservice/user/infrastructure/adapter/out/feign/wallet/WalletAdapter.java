package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// WalletPort 인터페이스의 FeignClient 기반 구현체 (Adapter)
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletAdapter implements WalletPort {

    private final WalletFeignClient walletFeignClient;

    // 외부 지갑 서비스에 유저 고유 ID 전달하여 지갑 생성 요청
    @Override
    public void createWallet(UserId userId) {
        log.info("Requesting wallet creation for user: {}", userId.value());
        try {
            WalletCreateRequest request = new WalletCreateRequest(userId.value());
            walletFeignClient.createWallet(request);
            log.info("Successfully requested wallet creation for user: {}", userId.value());
        } catch (Exception e) {
            log.error("Failed to request wallet creation for user: {}", userId.value(), e);
            throw new UserException(UserErrorCode.KEYCLOAK_ERROR); 
        }
    }
}
