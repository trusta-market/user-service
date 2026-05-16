package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.Role;

public interface IdentityProviderPort {
    // 외부 인증 기관에 새로운 사용자를 생성하고 KeycloakId를 반환합니다.
    KeycloakId createIdentity(Email email, String password, Name name);

    // 외부 인증 기관에서 사용자를 삭제합니다.
    void deleteIdentity(KeycloakId keycloakId);

    // 외부 인증 기관에서 사용자의 Realm 역할을 변경합니다. (DB 역할 변경 시 반드시 함께 호출)
    void assignRole(KeycloakId keycloakId, Role role);
}
