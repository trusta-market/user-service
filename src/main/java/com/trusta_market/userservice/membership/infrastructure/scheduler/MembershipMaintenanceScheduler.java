package com.trusta_market.userservice.membership.infrastructure.scheduler;

import com.trusta_market.userservice.membership.application.service.MembershipService;
import com.trusta_market.userservice.user.application.port.out.UserMembershipPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipMaintenanceScheduler {

    private final UserMembershipPointHistoryRepository pointHistoryRepository;
    private final MembershipService membershipService;

    /**
     * 매일 자정 실행.
     * 오늘 기준으로 딱 3개월 전 포인트가 만료되는 유저만 대상으로 등급을 재계산합니다.
     * (전체 유저를 순회하지 않아 쿼리 부하 최소화)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void recalculateExpiringUsers() {
        LocalDateTime to   = LocalDateTime.now().minusMonths(3);
        LocalDateTime from = to.minusDays(1);

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
                // 한 유저 실패가 전체 배치를 중단시키지 않도록 개별 처리
                log.error("[MembershipScheduler] 등급 재계산 실패 - userId={}, error={}", userId, e.getMessage(), e);
                failCount++;
            }
        }

        log.info("[MembershipScheduler] 완료 - 성공: {}, 실패: {}", successCount, failCount);
    }
}
