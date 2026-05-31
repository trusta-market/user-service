package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.entity.UserMembershipPointHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserMembershipPointHistoryRepository {

    UserMembershipPointHistory save(UserMembershipPointHistory history);

    /** 특정 유저의 최근 3개월 포인트 합산 (등급 재계산용) */
    int sumEarnedPointsSince(UUID userId, LocalDateTime since);

    /** 오늘 기준 3개월 전 포인트가 만료되는 유저 목록 조회 (스케줄러용) */
    List<UUID> findUserIdsWithExpiringPoints(LocalDateTime from, LocalDateTime to);
}
