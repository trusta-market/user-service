package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;

public interface IdentityProviderPort {
    /**
     * 외부 인증 기관에 새로운 사용자를 생성합니다.
     * @return 생성된 사용자의 고유 식별자 (KeycloakId)
     */
    KeycloakId createIdentity(Email email, String password, Name name);

    /**
     * 외부 인증 기관에서 사용자를 삭제합니다.
     */
    void deleteIdentity(KeycloakId keycloakId);
}
