package com.trusta_market.userservice.account.presentation.dto.request;

import com.trusta_market.userservice.account.domain.vo.AccountType;

// 계좌 생성 요청 객체
public record PostAccountRequest(
        String bankCode,      // 은행 코드
        String accountNumber, // 계좌 번호
        String accountHolder, // 예금주 명
        AccountType accountType // 계좌 타입
) {
}
