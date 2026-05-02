package com.trusta_market.userservice.account.application.service;

import com.trusta_market.userservice.account.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.account.application.dto.result.AccountResult;
import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.repository.UserAccountRepository;
import com.trusta_market.userservice.account.domain.vo.AccountType;
import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserValidationUseCase userValidationUseCase;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("계좌 등록 성공 - 첫 번째 계좌는 기본 계좌로 설정됨")
    void createAccount_Success() {
        // given
        UUID userId = UUID.randomUUID();
        CreateAccountCommand command = new CreateAccountCommand(userId, "001", "12345", "홍길동", AccountType.DEPOSIT);
        
        when(userAccountRepository.countActiveAccountsByUserId(userId)).thenReturn(0L);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        AccountResult result = accountService.createAccount(command);

        // then
        assertThat(result.isDefault()).isTrue();
        verify(userValidationUseCase).validateUserCanMutate(userId);
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    @Test
    @DisplayName("계좌 등록 실패 - 최대 5개 초과 시 에러")
    void createAccount_Fail_LimitExceeded() {
        // given
        UUID userId = UUID.randomUUID();
        CreateAccountCommand command = new CreateAccountCommand(userId, "001", "12345", "홍길동", AccountType.DEPOSIT);
        
        when(userAccountRepository.countActiveAccountsByUserId(userId)).thenReturn(5L);

        // when & then
        assertThatThrownBy(() -> accountService.createAccount(command))
                .isInstanceOf(DomainException.class);
    }
}
