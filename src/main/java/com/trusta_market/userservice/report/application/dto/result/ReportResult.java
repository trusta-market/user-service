package com.trusta_market.userservice.report.application.dto.result;

import com.trusta_market.userservice.report.domain.UserReport;
import java.util.UUID;

public record ReportResult(
    UUID reportId,
    UUID reporterUserId,
    UUID reportedUserId,
    String reason,
    String status
) {
    public static ReportResult from(UserReport report) {
        return new ReportResult(
            report.getReportId(),
            report.getReporterUserId(),
            report.getReportedUserId(),
            report.getReason(),
            report.getStatus().name()
        );
    }
}
