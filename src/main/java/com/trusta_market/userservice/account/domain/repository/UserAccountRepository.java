package com.trusta_market.userservice.account.domain.repository;

import com.trusta_market.userservice.account.domain.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository {
    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(UUID accountId);

    List<UserAccount> findAllActiveAccountsByUserId(UUID userId);

    long countActiveAccountsByUserId(UUID userId);

    Optional<UserAccount> findActiveAccountByIdAndUserId(UUID accountId, UUID userId);

    Optional<UserAccount> findDefaultAccountByUserId(UUID userId);

    void clearDefaultAccount(UUID userId);
}
