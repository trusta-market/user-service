package com.trusta_market.userservice.account.application.port.out;

import com.trusta_market.userservice.account.domain.vo.AccountHolder;
import com.trusta_market.userservice.account.domain.vo.AccountNumber;
import com.trusta_market.userservice.account.domain.vo.BankCode;

/**
 * 외부 뱅킹 API(예: Toss)를 통해 실명 및 계좌 유효성을 검증하기 위한 Outbound Port
 */
public interface AccountVerificationPort {
    /**
     * 계좌 정보의 유효성을 검증합니다.
     *
     * @param bankCode      은행 코드
     * @param accountNumber 계좌 번호
     * @param accountHolder 예금주 실명
     * @return 유효성 검증 성공 여부
     */
    boolean verifyAccount(BankCode bankCode, AccountNumber accountNumber, AccountHolder accountHolder);
}
