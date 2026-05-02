package com.trusta_market.userservice.account.presentation;

import com.trusta_market.userservice.account.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.account.application.port.in.AccountUseCase;
import com.trusta_market.userservice.account.presentation.dto.request.PostAccountRequest;
import com.trusta_market.userservice.account.presentation.dto.response.GetAccountResponse;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// 사용자 계좌 관리 외부 API 컨트롤러
@RestController
@RequestMapping("/api/v1/admin/accounts")
public class AccountApiController {

    private final AccountUseCase accountUseCase;

    public AccountApiController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    // 내 계좌 등록 API
    @PostMapping
    public ResponseEntity<GetAccountResponse> createAccount(@RequestBody PostAccountRequest request) {
        UUID userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        var result = accountUseCase.createAccount(new CreateAccountCommand(
                userId,
                request.bankCode(),
                request.accountNumber(),
                request.accountHolder(),
                request.accountType()));

        return ResponseEntity.status(201).body(GetAccountResponse.from(result));
    }

    // 내 계좌 목록 조회 API
    @GetMapping
    public ResponseEntity<List<GetAccountResponse>> getMyAccounts() {
        UUID userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        var results = accountUseCase.getAccountList(userId);
        return ResponseEntity.ok(results.stream()
                .map(GetAccountResponse::from)
                .toList());
    }

    // 대표 계좌 설정 변경 API
    @PatchMapping("/{accountId}/default")
    public ResponseEntity<Void> changeDefaultAccount(@PathVariable UUID accountId) {
        UUID userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        accountUseCase.changeAccountDefault(userId, accountId);
        return ResponseEntity.noContent().build();
    }

    // 계좌 삭제 (Soft Delete) API
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId) {
        UUID userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));

        accountUseCase.deleteAccount(userId, accountId);
        return ResponseEntity.noContent().build();
    }
}
