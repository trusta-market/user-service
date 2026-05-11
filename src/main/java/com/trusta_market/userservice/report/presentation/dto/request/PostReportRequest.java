package com.trusta_market.userservice.report.presentation.dto.request;

import java.util.UUID;

// 사용자 신고 요청 객체
public record PostReportRequest(
        UUID reportedUserId, // 신고 대상 유저 ID
        String reason        // 신고 사유
) {
}
