package com.trusta_market.userservice.account.application.service;

import com.trusta_market.userservice.account.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.account.application.dto.result.AccountResult;
import com.trusta_market.userservice.account.application.port.in.AccountUseCase;
import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.account.domain.repository.UserAccountRepository;
import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
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
        userValidationUseCase.validateUserCanMutate(command.userId());

        if (userAccountRepository.countActiveAccountsByUserId(command.userId()) >= 5) {
            throw new DomainException(AccountErrorCode.ACCOUNT_LIMIT_EXCEEDED);
        }

        boolean makeDefault = userAccountRepository.countActiveAccountsByUserId(command.userId()) == 0;
        UserAccount account = UserAccount.create(
                command.userId(), command.bankCode(), command.accountNumber(),
                command.accountHolder(), command.accountType(), makeDefault, false
        );

        return AccountResult.from(userAccountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResult> getAccountList(UUID userId) {
        return userAccountRepository.findAllActiveAccountsByUserId(userId)
                .stream()
                .map(AccountResult::from)
                .toList();
    }

    @Override
    public void deleteAccount(UUID userId, UUID accountId) {
        userValidationUseCase.validateUserCanMutate(userId);
        UserAccount account = findOwnedAccount(userId, accountId);
        account.delete(userId);
        userAccountRepository.save(account);
    }

    @Override
    public void changeAccountDefault(UUID userId, UUID accountId) {
        userValidationUseCase.validateUserCanMutate(userId);
        UserAccount account = findOwnedAccount(userId, accountId);
        userAccountRepository.clearDefaultAccount(userId);
        account.markAsDefault();
        userAccountRepository.save(account);
    }

    private UserAccount findOwnedAccount(UUID userId, UUID accountId) {
        return userAccountRepository.findActiveAccountByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
    }
}
