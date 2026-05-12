package com.trusta_market.userservice.report.application.dto.command;

import java.util.UUID;

public record CreateReportCommand(
    UUID reporterUserId,
    UUID reportedUserId,
    String reason
) {
}
