package com.trusta_market.userservice.report.infrastructure.persistence.jpa;

import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserReportRepositoryImpl implements UserReportRepository {

    private final UserReportJpaRepository userReportJpaRepository;

    public UserReportRepositoryImpl(UserReportJpaRepository userReportJpaRepository) {
        this.userReportJpaRepository = userReportJpaRepository;
    }

    @Override
    public UserReport save(UserReport report) {
        return userReportJpaRepository.save(report);
    }

    @Override
    public Optional<UserReport> findById(UUID reportId) {
        return userReportJpaRepository.findById(reportId);
    }

    @Override
    public boolean existsByReporterUserIdAndReportedUserId(UUID reporterUserId, UUID reportedUserId) {
        return userReportJpaRepository.existsByReporterUserIdAndReportedUserId(reporterUserId, reportedUserId);
    }

    @Override
    public List<UserReport> findAllByReporterUserId(UUID reporterUserId) {
        return userReportJpaRepository.findAllByReporterUserId(reporterUserId);
    }

    @Override
    public DomainPage<UserReport> findAllByStatus(DomainPageRequest pageRequest, ReportStatus status) {
        PageRequest springPageable = PageRequest.of(pageRequest.page(), pageRequest.size());
        Page<UserReport> page = userReportJpaRepository.findAllByStatus(springPageable, status);
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public DomainPage<UserReport> findAll(DomainPageRequest pageRequest) {
        PageRequest springPageable = PageRequest.of(pageRequest.page(), pageRequest.size());
        Page<UserReport> page = userReportJpaRepository.findAll(springPageable);
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
