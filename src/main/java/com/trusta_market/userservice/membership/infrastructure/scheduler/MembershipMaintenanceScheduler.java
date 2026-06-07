package com.trusta_market.userservice.membership.infrastructure.scheduler;

import com.trusta_market.userservice.membership.application.service.MembershipService;
import com.trusta_market.userservice.user.application.port.out.UserMembershipPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipMaintenanceScheduler {

    private final UserMembershipPointHistoryRepository pointHistoryRepository;
    private final MembershipService membershipService;

    @Scheduled(cron = "0 0 0 * * *")
    public void recalculateExpiringUsers() {
        Instant to   = Instant.now().minus(90, ChronoUnit.DAYS);
        Instant from = to.minus(1, ChronoUnit.DAYS);

        List<UUID> targetUserIds = pointHistoryRepository.findUserIdsWithExpiringPoints(from, to);

        if (targetUserIds.isEmpty()) {
            log.debug("[MembershipScheduler] 만료 포인트 대상 유저 없음");
            return;
        }

        log.info("[MembershipScheduler] 등급 재계산 대상 유저 수: {}", targetUserIds.size());

        int successCount = 0;
        int failCount = 0;

        for (UUID userId : targetUserIds) {
            try {
                membershipService.recalculateGradeByScheduler(userId);
                successCount++;
            } catch (Exception e) {
                log.error("[MembershipScheduler] 등급 재계산 실패 - userId={}, error={}", userId, e.getMessage(), e);
                failCount++;
            }
        }

        log.info("[MembershipScheduler] 완료 - 성공: {}, 실패: {}", successCount, failCount);
    }
}
