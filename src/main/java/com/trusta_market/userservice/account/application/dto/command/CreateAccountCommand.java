package com.trusta_market.userservice.account.application.dto.command;

import com.trusta_market.userservice.account.domain.vo.AccountType;
import java.util.UUID;

public record CreateAccountCommand(
    UUID userId,
    String bankCode,
    String accountNumber,
    String accountHolder,
    AccountType accountType
) {
}
