package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.vo.AccountType;

import java.util.UUID;

public record AccountInternalResult(
        UUID accountId,
        String bankCode,
        String accountNumber,
        String accountHolder,
        AccountType accountType,
        boolean isVerified
) {
    // 내부 조회용 계좌 결과로 변환한다.
    public static AccountInternalResult from(UserAccount account) {
        return new AccountInternalResult(
                account.getAccountId(),
                account.getBankCode().value(),
                maskAccountNumber(account.getAccountNumber().value()),
                account.getAccountHolder().value(),
                account.getAccountType(),
                account.isVerified()
        );
    }

    // 계좌번호의 뒤 3자리를 제외하고 마스킹한다.
    private static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) {
            return accountNumber;
        }
        String suffix = accountNumber.substring(Math.max(0, accountNumber.length() - 3));
        return "***-***-" + suffix;
    }
}
