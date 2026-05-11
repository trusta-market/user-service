package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet;

import com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto.WalletCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Wallet 서비스 API 호출을 위한 Feign Client
 */
@FeignClient(name = "wallet-service", url = "${app.services.wallet.url:http://wallet-service}")
public interface WalletFeignClient {

    /**
     * 내부 서버 간 통신을 통한 지갑 생성 API
     *
     * @param request 지갑 생성 요청 정보 (userId 포함)
     */
    @PostMapping("/internal/v1/wallets")
    void createWallet(@RequestBody WalletCreateRequest request);
}
