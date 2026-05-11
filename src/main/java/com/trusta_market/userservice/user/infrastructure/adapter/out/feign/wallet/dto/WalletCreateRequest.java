package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto;

import java.util.UUID;

/**
 * Wallet 서비스 지갑 생성 API 요청 DTO
 */
public record WalletCreateRequest(UUID userId) {
}
