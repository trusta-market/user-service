package com.trusta_market.userservice.membership.application.service;

import com.trusta_market.userservice.membership.application.port.in.MembershipPointUseCase;
import com.trusta_market.userservice.membership.application.port.in.MembershipUseCase;
import com.trusta_market.userservice.membership.domain.MembershipPointCalculator;
import com.trusta_market.userservice.user.application.dto.result.internal.MembershipFeeRateResult;
import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;
import com.trusta_market.userservice.user.application.port.out.UserMembershipHistoryRepository;
import com.trusta_market.userservice.user.application.port.out.UserMembershipPointHistoryRepository;
import com.trusta_market.userservice.user.application.port.out.UserRepository;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.entity.UserMembershipHistory;
import com.trusta_market.userservice.user.domain.entity.UserMembershipPointHistory;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.PointRole;
import com.trusta_market.userservice.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembershipService implements MembershipUseCase, MembershipPointUseCase {

    private final UserRepository userRepository;
    private final UserMembershipPointHistoryRepository pointHistoryRepository;
    private final UserMembershipHistoryRepository membershipHistoryRepository;

    // ── 조회 ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public MembershipResult getMembership(UUID userId) {
        return MembershipResult.from(findActiveUser(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipFeeRateResult getFeeRate(UUID userId) {
        return MembershipFeeRateResult.from(findActiveUser(userId));
    }

    // ── 포인트 지급 + 등급 재계산 ───────────────────────────────────────────

    @Override
    @Transactional
    public void processOrderConfirmed(UUID orderId, UUID buyerId, UUID sellerId, long amount) {
        int points = MembershipPointCalculator.calculate(amount);

        grantPointsAndRecalculate(buyerId, orderId, PointRole.BUYER, points);
        grantPointsAndRecalculate(sellerId, orderId, PointRole.SELLER, points);
    }

    /**
     * 단일 유저에게 포인트를 지급하고 최근 3개월 롤링 기준으로 등급을 재계산합니다.
     * findByIdWithLock 으로 비관적 락을 걸어 동시 이벤트로 인한 등급 충돌을 방지합니다.
     */
    @Transactional
    public void grantPointsAndRecalculate(UUID userId, UUID orderId, PointRole role, int points) {
        // 1. 포인트 이력 저장 (orderId + role 유니크 제약으로 중복 이벤트 멱등 처리)
        UserMembershipPointHistory history =
                UserMembershipPointHistory.create(userId, orderId, role, points);
        pointHistoryRepository.save(history);

        // 2. 최근 3개월 롤링 포인트 합산
        LocalDateTime since = LocalDateTime.now().minusMonths(3);
        int rollingPoints = pointHistoryRepository.sumEarnedPointsSince(userId, since);

        // 3. 등급 재계산 및 변동 시 업데이트
        recalculateGrade(userId, rollingPoints);
    }

    /**
     * 스케줄러에서 만료 포인트 대상 유저의 등급을 재계산할 때 사용합니다.
     */
    @Transactional
    public void recalculateGradeByScheduler(UUID userId) {
        LocalDateTime since = LocalDateTime.now().minusMonths(3);
        int rollingPoints = pointHistoryRepository.sumEarnedPointsSince(userId, since);
        recalculateGrade(userId, rollingPoints);
    }

    // ── 내부 공통 메서드 ────────────────────────────────────────────────────

    private void recalculateGrade(UUID userId, int rollingPoints) {
        User user = userRepository.findByIdWithLock(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Membership current = user.getMembership();
        Membership calculated = Membership.of(rollingPoints);

        if (current == calculated) {
            return;  // 등급 변동 없음
        }

        user.updateMembership(calculated);
        userRepository.save(user);

        membershipHistoryRepository.save(
                UserMembershipHistory.create(userId, current, calculated,
                        String.format("3달 롤링 포인트 %d점 → %s", rollingPoints, calculated.name()))
        );

        log.info("[Membership] 등급 변동 - userId={}, {} → {} (rollingPoints={})",
                userId, current, calculated, rollingPoints);
    }

    private User findActiveUser(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new UserException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }
}
