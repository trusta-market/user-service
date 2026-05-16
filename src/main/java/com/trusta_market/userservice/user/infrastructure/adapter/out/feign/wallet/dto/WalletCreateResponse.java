package com.trusta_market.userservice.user.infrastructure.adapter.out.feign.wallet.dto;

public record WalletCreateResponse(int status, WalletData data) {
    public record WalletData(boolean result) {}

    public boolean isSuccess() {
        return data != null && data.result();
    }
}
