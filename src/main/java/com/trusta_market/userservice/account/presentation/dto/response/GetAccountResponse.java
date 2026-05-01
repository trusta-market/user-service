package com.trusta_market.userservice.account.presentation.dto.response;

import com.trusta_market.userservice.account.application.dto.result.AccountResult;
import java.util.UUID;

// 계좌 정보 응답 객체
public record GetAccountResponse(
        UUID accountId,
        String bankCode,
        String accountNumber,
        String accountHolder,
        String accountType,
        boolean isDefault
) {
    // Result 객체로부터 Response 생성
    public static GetAccountResponse from(AccountResult result) {
        return new GetAccountResponse(
                result.accountId(),
                result.bankCode(),
                result.accountNumber(),
                result.accountHolder(),
                result.accountType(),
                result.isDefault());
    }
}
