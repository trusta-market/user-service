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

public interface UserUseCase {
    // Keycloak 계정 생성과 로컬 사용자 생성을 함께 처리한다.
    UserResult signUp(SignUpCommand command);

    // 내부 시스템 기준으로 사용자를 신규 생성한다.
    UserResult createUser(CreateUserCommand command);

    // Keycloak 식별자로 사용자 정보를 조회한다.
    UserResult getUserByKeycloakId(KeycloakId keycloakId);

    // 내부 사용자 식별자로 사용자 정보를 조회한다.
    UserResult getUser(UUID userId);

    // 사용자 프로필을 수정한다.
    UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command);

    // 사용자를 탈퇴 처리한다.
    void withdrawUser(KeycloakId keycloakId);

    // 관리자 조회용 사용자 목록을 페이지 단위로 반환한다.
    DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role);

    // 관리자 승인 대기 상태의 사용자를 승인한다.
    UserResult approveUser(UUID userId);

    // 관리자 승인 대기 상태의 사용자를 거절한다.
    UserResult rejectUser(UUID userId, String reason);

    // 사용자의 활성 배송지 목록을 조회한다.
    List<AddressResult> getAddressList(UUID userId);

    // 사용자의 배송지를 등록한다.
    AddressResult createAddress(CreateAddressCommand command);

    // 사용자의 배송지 정보를 수정한다.
    AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command);

    // 사용자의 배송지를 삭제한다.
    void deleteAddress(UUID userId, UUID addressId);

    // 사용자의 대표 배송지를 변경한다.
    void changeAddressDefault(UUID userId, UUID addressId);

    // 내부 서비스용 사용자 상세 정보를 조회한다.
    UserInternalResult getInternalUser(UUID userId);

    // 내부 서비스용 사용자 목록을 일괄 조회한다.
    List<UserInternalResult> getInternalUserList(List<UUID> userIds);

    // 내부 서비스 요청 전에 사용자 활성 상태를 검증한다.
    void validateInternalUser(UUID userId);
}
