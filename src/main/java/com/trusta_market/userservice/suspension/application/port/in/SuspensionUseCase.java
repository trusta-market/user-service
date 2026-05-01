package com.trusta_market.userservice.suspension.application.port.in;

import java.time.LocalDateTime;
import java.util.UUID;

// 유저 정지 관리 유스케이스 인터페이스
public interface SuspensionUseCase {
    // 유저 정지 이력 생성
    void suspendUser(UUID userId, String reason, LocalDateTime expiresAt);
    // 유저 정지 해제 처리
    void unsuspendUser(UUID userId, String reason);
}
