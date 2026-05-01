package com.trusta_market.userservice.account.infrastructure.persistence.jpa;

import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.repository.UserAccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountJpaRepository userAccountJpaRepository;

    public UserAccountRepositoryImpl(UserAccountJpaRepository userAccountJpaRepository) {
        this.userAccountJpaRepository = userAccountJpaRepository;
    }

    @Override
    public UserAccount save(UserAccount account) {
        return userAccountJpaRepository.save(account);
    }

    @Override
    public Optional<UserAccount> findById(UUID accountId) {
        return userAccountJpaRepository.findById(accountId);
    }

    @Override
    public List<UserAccount> findAllActiveAccountsByUserId(UUID userId) {
        return userAccountJpaRepository.findAllByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public long countActiveAccountsByUserId(UUID userId) {
        return userAccountJpaRepository.countByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public Optional<UserAccount> findActiveAccountByIdAndUserId(UUID accountId, UUID userId) {
        return userAccountJpaRepository.findByAccountIdAndUserIdAndDeletedAtIsNull(accountId, userId);
    }

    @Override
    public Optional<UserAccount> findDefaultAccountByUserId(UUID userId) {
        return userAccountJpaRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(userId);
    }

    @Transactional
    @Override
    public void clearDefaultAccount(UUID userId) {
        userAccountJpaRepository.findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(userId)
                .ifPresent(account -> account.setDefault(false));
    }
}
