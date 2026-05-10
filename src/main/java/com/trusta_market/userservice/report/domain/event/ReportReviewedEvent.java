package com.trusta_market.userservice.report.domain.event;

import java.util.UUID;

/**
 * 피신고자(reportedUserId)의 신뢰 점수 하락이나 알림 발송 처리를 타 바운디드 컨텍스트에 위임하기 위한 도메인 이벤트
 */
public record ReportReviewedEvent(UUID reportedUserId) {
}
