package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.UserMembershipPointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserMembershipPointHistoryJpaRepository extends JpaRepository<UserMembershipPointHistory, UUID> {

    /** 특정 유저의 since 이후 획득 포인트 합산 */
    @Query("SELECT COALESCE(SUM(h.earnedPoints), 0) FROM UserMembershipPointHistory h " +
           "WHERE h.userId = :userId AND h.createdAt >= :since")
    int sumEarnedPointsSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);

    /** from ~ to 사이에 포인트를 획득한 유저 목록 (스케줄러: 오늘 기준 만료 대상) */
    @Query("SELECT DISTINCT h.userId FROM UserMembershipPointHistory h " +
           "WHERE h.createdAt >= :from AND h.createdAt < :to")
    List<UUID> findDistinctUserIdsByCreatedAtBetween(@Param("from") LocalDateTime from,
                                                     @Param("to") LocalDateTime to);
}
