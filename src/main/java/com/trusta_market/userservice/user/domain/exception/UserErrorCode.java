package com.trusta_market.userservice.user.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 유저 도메인(유저, 계좌, 주소, 내부 서비스) 통합 에러 코드 정의 (프레임워크 독립적)
@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    // User Basic
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(400, "U002", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(400, "U003", "이미 사용 중인 이름입니다."),
    ALREADY_WITHDRAWN(400, "U004", "이미 탈퇴한 회원입니다."),
    PENDING_USER(403, "U005", "승인 대기 중인 회원입니다."),
    REJECTED_USER(403, "U006", "가입 신청이 거절된 회원입니다."),
    SUSPENDED_USER(403, "U007", "정지된 회원입니다."),
    USER_NOT_ACTIVE(403, "U008", "활성화된 사용자가 아닙니다."),

    // Validation
    INVALID_EMAIL_FORMAT(400, "V001", "올바르지 않은 이메일 형식입니다."),
    INVALID_NAME_FORMAT(400, "V002", "올바르지 않은 이름 형식입니다."),
    INVALID_PHONE_FORMAT(400, "V003", "올바르지 않은 전화번호 형식입니다."),
    INVALID_ZIP_CODE(400, "V004", "올바르지 않은 우편번호 형식입니다."),
    INVALID_INPUT(400, "V005", "올바르지 않은 입력값입니다."),
    INVALID_STATUS_TRANSITION(400, "V006", "변경 불가능한 유저 상태입니다."),
    INVALID_REALNAME_FORMAT(400, "V007", "올바르지 않은 실명 형식입니다."),

    // Address
    ADDRESS_NOT_FOUND(404, "A001", "주소를 찾을 수 없습니다."),
    ADDRESS_LIMIT_EXCEEDED(400, "A002", "등록 가능한 주소 개수를 초과했습니다."),

    // Account
    ACCOUNT_NOT_FOUND(404, "AC01", "계좌 정보를 찾을 수 없습니다."),
    ACCOUNT_LIMIT_EXCEEDED(400, "AC02", "계좌 생성 한도(5개)를 초과했습니다."),
    ACCOUNT_NOT_VERIFIED(400, "AC03", "인증되지 않은 계좌입니다."),

    // Internal Infrastructure
    KEYCLOAK_ERROR(500, "I001", "인증 서버 연동에 실패했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
