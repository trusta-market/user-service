package com.trusta_market.userservice.suspension.application.service;

import com.trusta_market.userservice.suspension.application.port.in.SuspensionUseCase;
import com.trusta_market.userservice.suspension.domain.UserSuspension;
import com.trusta_market.userservice.suspension.domain.repository.UserSuspensionRepository;
import com.trusta_market.userservice.user.application.port.out.UserRepository;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

// 유저 정지 관리 비즈니스 로직 서비스
@Service
@Transactional
public class SuspensionService implements SuspensionUseCase {

    private final UserSuspensionRepository suspensionRepository;
    private final UserRepository userRepository;

    public SuspensionService(UserSuspensionRepository suspensionRepository,
                             UserRepository userRepository) {
        this.suspensionRepository = suspensionRepository;
        this.userRepository = userRepository;
    }

    // 유저 정지 처리 및 이력 저장
    @Override
    public void suspendUser(UUID userId, String reason, LocalDateTime expiresAt) {
        User user = findUser(userId);
        user.suspend();
        userRepository.save(user);

        UserSuspension suspension = UserSuspension.create(userId, reason, expiresAt);
        suspensionRepository.save(suspension);
    }

    // 유저 정지 해제 처리
    @Override
    public void unsuspendUser(UUID userId, String reason) {
        User user = findUser(userId);
        user.unsuspend();
        userRepository.save(user);

        suspensionRepository.findAllByUserId(userId).stream()
                .filter(suspension -> suspension.getReleasedAt() == null)
                .max(Comparator.comparing(UserSuspension::getCreatedAt))
                .ifPresent(suspension -> {
                    suspension.release(SecurityUtil.getCurrentUserId().orElse(null), reason);
                    suspensionRepository.save(suspension);
                });
    }

    private User findUser(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new UserException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }
}
