package com.trusta_market.userservice.account.domain.repository;

import com.trusta_market.userservice.account.domain.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자 계좌(UserAccount) 도메인 리포지토리 인터페이스 (Port)
 * <p>
 * 서비스 로직이 의존하는 순수 도메인 인터페이스입니다.
 * 실제 DB 통신(JPA 등)은 이 인터페이스를 구현하는 인프라 계층의 Adapter(RepositoryImpl)가 담당합니다.
 * </p>
 */
public interface UserAccountRepository {
    UserAccount save(UserAccount account);

    Optional<UserAccount> findById(UUID accountId);

    List<UserAccount> findAllActiveAccountsByUserId(UUID userId);

    long countActiveAccountsByUserId(UUID userId);

    Optional<UserAccount> findActiveAccountByIdAndUserId(UUID accountId, UUID userId);

    Optional<UserAccount> findDefaultAccountByUserId(UUID userId);

    void clearDefaultAccount(UUID userId);
    
    Optional<UserAccount> findActiveAccountById(UUID accountId);
}
