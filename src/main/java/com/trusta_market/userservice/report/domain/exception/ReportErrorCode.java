package com.trusta_market.userservice.report.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 신고 도메인 전역에서 사용하는 에러 코드 정의
 * 프로젝트 표준 가이드라인에 따라 ErrorCodeSpec 인터페이스를 구현합니다.
 */
@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements ErrorCodeSpec {
    SELF_REPORT(HttpStatus.BAD_REQUEST, "R001", "자기 자신은 신고할 수 없습니다."),
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "R002", "이미 신고한 사용자입니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "R003", "신고 내역을 찾을 수 없습니다."),
    ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "R004", "이미 처리된 신고입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "R005", "신고 사유가 유효하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public String getField() {
        return null;
    }
}
