package com.trusta_market.userservice.account.infrastructure.adapter.out.toss;

import com.trusta_market.userservice.account.application.port.out.AccountVerificationPort;
import com.trusta_market.userservice.account.domain.vo.AccountHolder;
import com.trusta_market.userservice.account.domain.vo.AccountNumber;
import com.trusta_market.userservice.account.domain.vo.BankCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 외부 토스(Toss) 뱅킹 API를 연동하여 계좌 유효성을 검증하는 Adapter.
 * 현재는 실제 API 연동 전이므로, 무조건 성공(true)을 반환하는 Dummy 구현체로 동작합니다.
 */
@Slf4j
@Component
public class DummyTossAccountAdapter implements AccountVerificationPort {

    @Override
    public boolean verifyAccount(BankCode bankCode, AccountNumber accountNumber, AccountHolder accountHolder) {
        // 실제 운영 환경에서는 Toss API(또는 오픈뱅킹 API)로 HTTP 요청을 보내서 실명과 계좌번호가 일치하는지 검증합니다.
        log.info("[Dummy Toss Adapter] 계좌 실명 검증 요청 - Bank: {}, Account: {}, Holder: {}",
                bankCode.value(), accountNumber.value(), accountHolder.value());

        // 로컬 테스트용: 특정 계좌번호(예: 9999)가 들어오면 무조건 실패하도록 설정해 볼 수도 있습니다.
        if (accountNumber.value().contains("9999")) {
            log.warn("[Dummy Toss Adapter] 테스트용 실패 계좌번호 감지");
            return false;
        }

        log.info("[Dummy Toss Adapter] 계좌 실명 검증 성공!");
        return true;
    }
}
