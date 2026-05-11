package com.trusta_market.userservice.account.infrastructure.persistence.jpa;

import com.trusta_market.userservice.account.domain.UserAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountJpaRepository extends JpaRepository<UserAccount, UUID> {
    List<UserAccount> findAllByUserIdAndDeletedAtIsNull(UUID userId);
    
    long countByUserIdAndDeletedAtIsNull(UUID userId);
    
    Optional<UserAccount> findByAccountIdAndUserIdAndDeletedAtIsNull(UUID accountId, UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserAccount> findByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UUID userId);

    Optional<UserAccount> findReadOnlyByUserIdAndIsDefaultTrueAndDeletedAtIsNull(UUID userId);

    Optional<UserAccount> findByAccountIdAndDeletedAtIsNull(UUID accountId);
}
