package com.trusta_market.userservice.user.application.port.in;

import java.util.UUID;

public interface UserValidationUseCase {
    // 유저 활성 상태(APPROVED) 여부 검증
    void validateActiveUser(UUID userId);

    // 유저 데이터 변경 가능 상태 여부 검증
    void validateUserCanMutate(UUID userId);

    // Keycloak ID를 내부 식별자(UUID)로 변환
    UUID resolveInternalId(UUID keycloakId);
}
