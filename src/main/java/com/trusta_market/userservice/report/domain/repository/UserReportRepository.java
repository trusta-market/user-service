package com.trusta_market.userservice.report.domain.repository;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.Optional;
import java.util.UUID;

// 사용자 신고 리포지토리 인터페이스
public interface UserReportRepository {
    // 신고 저장
    UserReport save(UserReport report);
    // 특정 신고 조회
    Optional<UserReport> findById(UUID reportId);
    // 동일인 중복 신고 여부 확인
    boolean existsByReporterUserIdAndReportedUserId(UserId reporterUserId, UserId reportedUserId);
    // 전체 신고 목록 페이징 조회 (상태 필터링 포함)
    DomainPage<UserReport> findAll(DomainPageRequest pageRequest, ReportStatus status);
    // 특정 신고자의 전체 신고 내역 조회
    java.util.List<UserReport> findAllByReporterUserId(UserId reporterUserId);
}
