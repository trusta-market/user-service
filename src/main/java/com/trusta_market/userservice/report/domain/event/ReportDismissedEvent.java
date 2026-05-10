package com.trusta_market.userservice.report.domain.event;

import java.util.UUID;

/**
 * 피신고자(reportedUserId)의 신고가 기각되었음을 알리기 위한 도메인 이벤트
 */
public record ReportDismissedEvent(UUID reportedUserId) {
}
