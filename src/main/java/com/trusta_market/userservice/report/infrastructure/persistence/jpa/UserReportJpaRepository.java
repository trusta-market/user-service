package com.trusta_market.userservice.report.infrastructure.persistence.jpa;

import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserReportJpaRepository extends JpaRepository<UserReport, UUID> {
    boolean existsByReporterUserIdAndReportedUserId(UUID reporterUserId, UUID reportedUserId);
    List<UserReport> findAllByReporterUserId(UUID reporterUserId);
    Page<UserReport> findAllByStatus(Pageable pageable, ReportStatus status);
}
