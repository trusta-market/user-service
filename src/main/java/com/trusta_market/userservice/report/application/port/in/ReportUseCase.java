package com.trusta_market.userservice.report.application.port.in;

import com.trusta_market.userservice.report.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.report.application.dto.result.ReportResult;

public interface ReportUseCase {
    ReportResult createReport(CreateReportCommand command);
}
