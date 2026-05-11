package com.trusta_market.userservice.report.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 신고 도메인 전용 에러 코드 정의 (프레임워크 독립적)
@Getter
@RequiredArgsConstructor
public enum ReportErrorCode {
    SELF_REPORT(400, "R001", "자기 자신은 신고할 수 없습니다."),
    DUPLICATE_REPORT(400, "R002", "이미 신고한 사용자입니다."),
    REPORT_NOT_FOUND(404, "R003", "신고 내역을 찾을 수 없습니다."),
    ALREADY_PROCESSED(400, "R004", "이미 처리된 신고입니다."),
    INVALID_INPUT(400, "R005", "신고 사유가 유효하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;
}
