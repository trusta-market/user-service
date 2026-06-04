package com.trusta_market.userservice.account.presentation;

import com.trusta_market.userservice.account.application.dto.result.AccountResult;
import com.trusta_market.userservice.account.application.port.in.AccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 타 마이크로서비스(예: Payment Service) 전용 내부 API 컨트롤러
 */
@RestController
@RequestMapping("/internal/v1/accounts")
@RequiredArgsConstructor
public class InternalAccountApiController {

    private final AccountUseCase accountUseCase;

    /**
     * 특정 사용자의 인증된 기본 계좌 정보 조회
     * 결제 서비스 등에서 실제 송금/결제 처리를 위해 사용합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 인증된 기본 계좌 정보
     */
    @GetMapping("/verified-default/{userId}")
    public ResponseEntity<CommonResponse<AccountResult>> getVerifiedDefaultAccount(@PathVariable UUID userId) {
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), accountUseCase.getVerifiedDefaultAccount(userId)));
    }
}
