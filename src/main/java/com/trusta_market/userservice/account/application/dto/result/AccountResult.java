package com.trusta_market.userservice.account.application.dto.result;

import com.trusta_market.userservice.account.domain.UserAccount;
import java.util.UUID;

public record AccountResult(
    UUID accountId,
    UUID userId,
    String bankCode,
    String accountNumber,
    String accountHolder,
    String accountType,
    boolean isDefault
) {
    public static AccountResult from(UserAccount account) {
        return new AccountResult(
            account.getAccountId(),
            account.getUserId(),
            account.getBankCode(),
            account.getAccountNumber(),
            account.getAccountHolder(),
            account.getAccountType().name(),
            account.isDefault()
        );
    }
}
