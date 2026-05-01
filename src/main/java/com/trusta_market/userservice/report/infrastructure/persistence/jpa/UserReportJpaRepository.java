package com.trusta_market.userservice.report.infrastructure.persistence.jpa;

import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// 사용자 신고 JPA 리포지토리
public interface UserReportJpaRepository extends JpaRepository<UserReport, UUID> {
    // 중복 신고 여부 확인
    boolean existsByReporterUserIdAndReportedUserId(UUID reporterUserId, UUID reportedUserId);
    // 상태별 페이징 조회
    Page<UserReport> findAllByStatus(ReportStatus status, Pageable pageable);
}
