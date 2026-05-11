package com.trusta_market.userservice.report.infrastructure.persistence.jpa;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// 사용자 신고 리포지토리 구현체
@Repository
public class UserReportRepositoryImpl implements UserReportRepository {

    private final UserReportJpaRepository userReportJpaRepository;

    public UserReportRepositoryImpl(UserReportJpaRepository userReportJpaRepository) {
        this.userReportJpaRepository = userReportJpaRepository;
    }

    // 신고 정보 저장
    @Override
    public UserReport save(UserReport report) {
        return userReportJpaRepository.save(report);
    }

    // 신고 ID로 단건 조회
    @Override
    public Optional<UserReport> findById(UUID reportId) {
        return userReportJpaRepository.findById(reportId);
    }

    // 신고자와 피신고자 ID로 중복 신고 여부 확인
    @Override
    public boolean existsByReporterUserIdAndReportedUserId(UserId reporterUserId, UserId reportedUserId) {
        return userReportJpaRepository.existsByReporterUserIdAndReportedUserId(reporterUserId, reportedUserId);
    }

    // 전체 혹은 상태별 신고 목록 페이징 조회
    @Override
    public DomainPage<UserReport> findAll(DomainPageRequest pageRequest, ReportStatus status) {
        Page<UserReport> page;
        if (status != null) {
            // 상탯값이 있으면 상태별 조회
            page = userReportJpaRepository.findAllByStatus(status, PageRequest.of(pageRequest.page(), pageRequest.size()));
        } else {
            // 상탯값이 없으면 전체 조회
            page = userReportJpaRepository.findAll(PageRequest.of(pageRequest.page(), pageRequest.size()));
        }
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
