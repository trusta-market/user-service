package com.trusta_market.userservice.account.presentation;

import com.trusta_market.userservice.account.application.port.in.AccountUseCase;
import com.trusta_market.userservice.account.presentation.dto.request.AdminRejectAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 관리자용 계좌 관리 API 컨트롤러
@RestController
@RequestMapping("/api/v1/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountApiController {

    private final AccountUseCase accountUseCase;

    public AdminAccountApiController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    // 계좌 인증 처리 API
    @PostMapping("/{accountId}/verify")
    public ResponseEntity<Void> verifyAccount(@PathVariable UUID accountId) {
        accountUseCase.verifyAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    // 계좌 인증 거절 API
    @PostMapping("/{accountId}/reject")
    public ResponseEntity<Void> rejectAccount(
            @PathVariable UUID accountId,
            @RequestBody AdminRejectAccountRequest request) {
        accountUseCase.rejectAccount(accountId, request.reason());
        return ResponseEntity.noContent().build();
    }
}
