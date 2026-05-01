package com.trusta_market.userservice.report.domain.repository;

import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserReportRepository {
    UserReport save(UserReport report);
    Optional<UserReport> findById(UUID reportId);
    boolean existsByReporterUserIdAndReportedUserId(UUID reporterUserId, UUID reportedUserId);
    List<UserReport> findAllByReporterUserId(UUID reporterUserId);
    DomainPage<UserReport> findAllByStatus(DomainPageRequest pageRequest, ReportStatus status);
    DomainPage<UserReport> findAll(DomainPageRequest pageRequest);
}
