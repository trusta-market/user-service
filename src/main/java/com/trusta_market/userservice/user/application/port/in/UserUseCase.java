package com.trusta_market.userservice.user.application.port.in;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.user.application.dto.command.CreateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.CreateUserCommand;
import com.trusta_market.userservice.user.application.dto.command.SignUpCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateUserCommand;
import com.trusta_market.userservice.user.application.dto.result.AddressResult;
import com.trusta_market.userservice.user.application.dto.result.UserResult;
import com.trusta_market.userservice.user.application.dto.result.internal.UserInternalResult;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;

import java.util.List;
import java.util.UUID;

// 유저 도메인의 핵심 비즈니스 유스케이스를 정의하는 인바운드 포트 (회원가입, 프로필, 배송지 관리 등)
public interface UserUseCase {
    // Keycloak 계정 생성 및 사용자 생성
    UserResult signUp(SignUpCommand command);

    // 내부 시스템 사용자 생성
    UserResult createUser(CreateUserCommand command);

    // Keycloak ID를 내부 식별자(UUID)로 변환
    UserResult getUserByKeycloakId(KeycloakId keycloakId);

    // 내부 사용자 식별자로 정보 조회
    UserResult getUser(UUID userId);

    // 사용자 프로필 수정
    UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command);

    // 사용자 탈퇴 처리
    void withdrawUser(KeycloakId keycloakId);

    // 관리자 조회용 사용자 목록 페이지 조회
    DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role);

    // 관리자 승인 대기 상태 사용자 승인
    UserResult approveUser(UUID userId);

    // 관리자 승인 대기 상태 사용자 거절
    UserResult rejectUser(UUID userId, String reason);

    // 사용자의 배송지 목록을 조회한다.
    List<AddressResult> getAddressList(UUID userId);

    // 사용자 배송지 등록
    AddressResult createAddress(CreateAddressCommand command);

    // 사용자 배송지 정보 수정
    AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command);

    // 사용자의 배송지를 삭제한다.
    void deleteAddress(UUID userId, UUID addressId);

    // 사용자의 대표 배송지를 변경한다.
    void changeAddressDefault(UUID userId, UUID addressId);

    // 내부 서비스용 사용자 상세 정보를 조회한다.
    UserInternalResult getInternalUser(UUID userId);

    // 내부 서비스용 사용자 목록을 일괄 조회한다.
    List<UserInternalResult> getInternalUserList(List<UUID> userIds);

    // 내부 서비스 요청 전 사용자 활성 상태 검증
    void validateInternalUser(UUID userId);
}
