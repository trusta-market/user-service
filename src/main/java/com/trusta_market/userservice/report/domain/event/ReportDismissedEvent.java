package com.trusta_market.userservice.report.domain.event;

import com.trusta_market.userservice.user.domain.vo.UserId;

/**
 * 피신고자(reportedUserId)의 신고가 기각되었음을 알리기 위한 도메인 이벤트
 */
public record ReportDismissedEvent(UserId reportedUserId) {
}
