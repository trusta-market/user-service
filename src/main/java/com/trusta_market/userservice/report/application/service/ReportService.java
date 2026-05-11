package com.trusta_market.userservice.report.application.service;

import com.trusta_market.userservice.report.domain.exception.ReportException;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.dto.result.ReportResult;
import com.trusta_market.userservice.report.application.port.in.ReportUseCase;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.report.domain.vo.ReportReason;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import com.trusta_market.userservice.user.domain.vo.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

// 사용자 신고 비즈니스 로직 서비스
@Service
@Transactional
public class ReportService implements ReportUseCase {

    private final UserReportRepository userReportRepository;
    private final UserValidationUseCase userValidationUseCase;

    public ReportService(UserReportRepository userReportRepository,
                         UserValidationUseCase userValidationUseCase) {
        this.userReportRepository = userReportRepository;
        this.userValidationUseCase = userValidationUseCase;
    }

    // 신규 신고 접수 로직
    @Override
    public ReportResult createReport(CreateReportCommand command) {
        // 신고자 및 피신고자 유효성 검증
        userValidationUseCase.validateUserCanMutate(command.reporterUserId());
        userValidationUseCase.validateActiveUser(command.reportedUserId());

        // 자기 자신 신고 여부 확인
        if (command.reporterUserId().equals(command.reportedUserId())) {
            throw new ReportException(ReportErrorCode.SELF_REPORT);
        }

        UserId reporterId = UserId.of(command.reporterUserId());
        UserId reportedId = UserId.of(command.reportedUserId());

        // 중복 신고 여부 확인
        if (userReportRepository.existsByReporterUserIdAndReportedUserId(reporterId, reportedId)) {
            throw new ReportException(ReportErrorCode.DUPLICATE_REPORT);
        }

        // 신고 엔티티 생성 및 저장
        UserReport report = UserReport.create(reporterId, reportedId, ReportReason.of(command.reason()));
        return ReportResult.from(userReportRepository.save(report));
    }

    // 신고 목록 페이징 조회 (관리자용)
    @Override
    @Transactional(readOnly = true)
    public DomainPage<ReportResult> getReportPage(int page, int size, ReportStatus status) {
        return userReportRepository.findAll(DomainPageRequest.of(page, size), status)
                .map(ReportResult::from);
    }

    // 신고 검토 완료 처리 (관리자용)
    @Override
    public ReportResult reviewReport(UUID reportId, UUID reviewedBy) {
        UserReport report = findReport(reportId);
        report.review(UserId.of(reviewedBy), java.time.LocalDateTime.now());
        return ReportResult.from(userReportRepository.save(report));
    }

    // 신고 기각 처리 (관리자용)
    @Override
    public ReportResult dismissReport(UUID reportId, UUID reviewedBy) {
        UserReport report = findReport(reportId);
        report.dismiss(UserId.of(reviewedBy), java.time.LocalDateTime.now());
        return ReportResult.from(userReportRepository.save(report));
    }

    // 신고 내역 단건 조회 (내부 헬퍼)
    private UserReport findReport(UUID reportId) {
        return userReportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
    }
}
