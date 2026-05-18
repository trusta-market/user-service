package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.vo.KeycloakId;

/**
 * 외부 지갑 서비스(Wallet Service) 연동을 위한 출력 포트
 */
public interface WalletPort {
    /**
     * 신규 사용자를 위한 지갑 생성 요청
     *
     * @param keycloakId 생성할 지갑의 소유자 Keycloak ID
     * @return 성공 여부
     */
    boolean createWallet(KeycloakId keycloakId);
}
