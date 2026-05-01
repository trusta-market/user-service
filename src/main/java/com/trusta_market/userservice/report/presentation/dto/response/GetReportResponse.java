package com.trusta_market.userservice.report.presentation.dto.response;

import com.trusta_market.userservice.report.application.dto.result.ReportResult;
import java.util.UUID;

// 신고 결과 응답 객체
public record GetReportResponse(
        UUID reportId,
        UUID reporterUserId,
        UUID reportedUserId,
        String reason,
        String status
) {
    // Result 객체로부터 Response 생성
    public static GetReportResponse from(ReportResult result) {
        return new GetReportResponse(
                result.reportId(),
                result.reporterUserId(),
                result.reportedUserId(),
                result.reason(),
                result.status());
    }
}
