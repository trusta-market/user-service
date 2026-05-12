package com.trusta_market.userservice.report.application.port.in;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.dto.result.ReportResult;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;

import java.util.UUID;

// 사용자 신고 유스케이스 인터페이스
public interface ReportUseCase {
    // 신고 접수
    ReportResult createReport(CreateReportCommand command);
    // 신고 목록 페이징 조회 (관리자용)
    DomainPage<ReportResult> getReportPage(int page, int size, ReportStatus status);
    // 신고 처리 (관리자용: 검토 완료)
    ReportResult reviewReport(UUID reportId, UUID reviewedBy);
    // 신고 기각 (관리자용: 반려)
    ReportResult dismissReport(UUID reportId, UUID reviewedBy);
}
