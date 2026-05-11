package com.trusta_market.userservice.suspension.application.service;

import com.trusta_market.userservice.suspension.application.port.in.SuspensionUseCase;
import com.trusta_market.userservice.suspension.domain.UserSuspension;
import com.trusta_market.userservice.suspension.domain.repository.UserSuspensionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// 유저 정지 관리 비즈니스 로직 서비스
@Service
@Transactional
public class SuspensionService implements SuspensionUseCase {

    private final UserSuspensionRepository suspensionRepository;

    public SuspensionService(UserSuspensionRepository suspensionRepository) {
        this.suspensionRepository = suspensionRepository;
    }

    // 유저 정지 처리 및 이력 저장
    @Override
    public void suspendUser(UUID userId, String reason, LocalDateTime expiresAt) {
        UserSuspension suspension = UserSuspension.create(userId, reason, expiresAt);
        suspensionRepository.save(suspension);
    }

    // 유저 정지 해제 처리
    @Override
    public void unsuspendUser(UUID userId, String reason) {
        // 기존 활성화된 정지 이력을 만료 처리하는 로직 등이 추가될 수 있음
    }
}
