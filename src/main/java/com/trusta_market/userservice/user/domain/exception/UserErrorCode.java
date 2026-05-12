package com.trusta_market.userservice.user.domain.exception;

import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 유저 도메인(유저, 계좌, 주소, 내부 서비스) 통합 에러 코드 정의
 * 프로젝트 표준 가이드라인에 따라 ErrorCodeSpec 인터페이스를 구현합니다.
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCodeSpec {
    // User Basic
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "U003", "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U004", "이미 사용 중인 이름입니다."),
    ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "U005", "이미 탈퇴한 회원입니다."),
    PENDING_USER(HttpStatus.FORBIDDEN, "U006", "승인 대기 중인 회원입니다."),
    REJECTED_USER(HttpStatus.FORBIDDEN, "U007", "가입 신청이 거절된 회원입니다."),
    SUSPENDED_USER(HttpStatus.FORBIDDEN, "U008", "정지된 회원입니다."),
    USER_NOT_ACTIVE(HttpStatus.FORBIDDEN, "U001", "활성화된 사용자가 아닙니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "U013", "변경 불가능한 유저 상태입니다."),

    // Validation
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "V001", "올바르지 않은 이메일 형식입니다."),
    INVALID_NAME_FORMAT(HttpStatus.BAD_REQUEST, "V002", "올바르지 않은 이름 형식입니다."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "V003", "올바르지 않은 전화번호 형식입니다."),
    INVALID_ZIP_CODE(HttpStatus.BAD_REQUEST, "V004", "올바르지 않은 우편번호 형식입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "V005", "올바르지 않은 입력값입니다."),
    INVALID_REALNAME_FORMAT(HttpStatus.BAD_REQUEST, "V007", "올바르지 않은 실명 형식입니다."),

    // Address
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "주소를 찾을 수 없습니다."),
    ADDRESS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "A002", "등록 가능한 주소 개수를 초과했습니다."),

    // Account
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "AC01", "계좌 정보를 찾을 수 없습니다."),
    ACCOUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "AC02", "계좌 생성 한도(5개)를 초과했습니다."),
    ACCOUNT_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AC03", "인증되지 않은 계좌입니다."),
    INVALID_ACCOUNT_HOLDER(HttpStatus.BAD_REQUEST, "AC04", "올바르지 않은 예금주 형식입니다."),
    INVALID_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST, "AC05", "올바르지 않은 계좌번호 형식입니다."),
    INVALID_BANK_CODE(HttpStatus.BAD_REQUEST, "AC06", "올바르지 않은 은행 코드입니다."),

    // Internal Infrastructure
    KEYCLOAK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "I001", "인증 서버 연동에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public String getField() {
        return null;
    }
}
