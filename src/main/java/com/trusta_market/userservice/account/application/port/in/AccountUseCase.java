package com.trusta_market.userservice.account.application.port.in;

import com.trusta_market.userservice.account.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.account.application.dto.result.AccountResult;

import java.util.List;
import java.util.UUID;

public interface AccountUseCase {
    AccountResult createAccount(CreateAccountCommand command);
    List<AccountResult> getAccountList(UUID userId);
    void deleteAccount(UUID userId, UUID accountId);
    void changeAccountDefault(UUID userId, UUID accountId);
    
    // Admin features
    void verifyAccount(UUID accountId);
    void rejectAccount(UUID accountId, String reason);
}
