package com.trusta_market.userservice.user.application.port.in;

import java.util.UUID;

public interface UserValidationUseCase {
    // 사용자가 활성 상태인지 검증한다.
    void validateActiveUser(UUID userId);

    // 사용자가 수정 가능한 상태인지 검증한다.
    void validateUserCanMutate(UUID userId);

    // Keycloak 식별자를 내부 사용자 식별자로 변환한다.
    UUID resolveInternalId(UUID keycloakId);
}
