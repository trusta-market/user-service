package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.application.port.out.WalletPort;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import feign.FeignException;
import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WalletAdapter implements WalletPort {

    private final WalletFeignClient walletFeignClient;

    @Override
    public boolean createWallet(KeycloakId keycloakId) {
        log.info("Requesting wallet creation for keycloakId: {}", keycloakId.value());
        try {
            WalletCreateResponse response = walletFeignClient.createWallet(
                    new WalletCreateRequest(UUID.fromString(keycloakId.value())));
            if (response.isSuccess()) {
                log.info("Successfully completed wallet creation for keycloakId: {}", keycloakId.value());
            }
            return response.isSuccess();
        } catch (FeignException e) {
            if (e.status() == 400 || e.status() == 409) {
                log.info("Wallet already exists for keycloakId: {}", keycloakId.value());
                return true;
            }
            log.error("Failed to request wallet creation for keycloakId: {}. status: {}, body: {}",
                    keycloakId.value(), e.status(), e.contentUTF8(), e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during wallet creation for keycloakId: {}", keycloakId.value(), e);
            return false;
        }
    }
}
