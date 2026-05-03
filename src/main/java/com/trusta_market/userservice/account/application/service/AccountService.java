package com.trusta_market.userservice.account.application.service;

import com.trusta_market.userservice.account.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.account.application.dto.result.AccountResult;
import com.trusta_market.userservice.account.application.port.in.AccountUseCase;
import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.account.domain.repository.UserAccountRepository;
import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService implements AccountUseCase {

    private final UserAccountRepository userAccountRepository;
    private final UserValidationUseCase userValidationUseCase;

    public AccountService(UserAccountRepository userAccountRepository,
                         UserValidationUseCase userValidationUseCase) {
        this.userAccountRepository = userAccountRepository;
        this.userValidationUseCase = userValidationUseCase;
    }

    @Override
    public AccountResult createAccount(CreateAccountCommand command) {
        UUID internalUserId = userValidationUseCase.resolveInternalId(command.userId());
        userValidationUseCase.validateUserCanMutate(internalUserId);

        if (userAccountRepository.countActiveAccountsByUserId(internalUserId) >= 5) {
            throw new DomainException(AccountErrorCode.ACCOUNT_LIMIT_EXCEEDED);
        }

        boolean makeDefault = userAccountRepository.countActiveAccountsByUserId(internalUserId) == 0;
        UserAccount account = UserAccount.create(
                internalUserId, command.bankCode(), command.accountNumber(),
                command.accountHolder(), command.accountType(), makeDefault, false
        );

        return AccountResult.from(userAccountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResult> getAccountList(UUID userId) {
        UUID internalUserId = userValidationUseCase.resolveInternalId(userId);
        return userAccountRepository.findAllActiveAccountsByUserId(internalUserId)
                .stream()
                .map(AccountResult::from)
                .toList();
    }

    @Override
    public void deleteAccount(UUID userId, UUID accountId) {
        UUID internalUserId = userValidationUseCase.resolveInternalId(userId);
        userValidationUseCase.validateUserCanMutate(internalUserId);
        UserAccount account = findOwnedAccount(internalUserId, accountId);
        account.delete(internalUserId);
        userAccountRepository.save(account);
    }

    @Override
    public void changeAccountDefault(UUID userId, UUID accountId) {
        UUID internalUserId = userValidationUseCase.resolveInternalId(userId);
        userValidationUseCase.validateUserCanMutate(internalUserId);
        UserAccount account = findOwnedAccount(internalUserId, accountId);
        userAccountRepository.clearDefaultAccount(internalUserId);
        account.markAsDefault();
        userAccountRepository.save(account);
    }

    @Override
    public void verifyAccount(UUID accountId) {
        UserAccount account = userAccountRepository.findActiveAccountById(accountId)
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
        
        // 실제로는 관리자 ID를 넣어야 하지만, 우선 현재 인증된 유저(관리자) ID 사용
        UUID adminId = SecurityUtil.getCurrentUserId().orElse(null);
        account.approve(adminId);
        userAccountRepository.save(account);
    }

    @Override
    public void rejectAccount(UUID accountId, String reason) {
        UserAccount account = userAccountRepository.findActiveAccountById(accountId)
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
        
        // 반려 로직 (현재 엔티티에 상태 필드가 없으므로 검증 해제로 처리하거나 로그 기록)
        account.delete(SecurityUtil.getCurrentUserId().orElse(null));
        userAccountRepository.save(account);
    }

    private UserAccount findOwnedAccount(UUID userId, UUID accountId) {
        return userAccountRepository.findActiveAccountByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
    }
}
