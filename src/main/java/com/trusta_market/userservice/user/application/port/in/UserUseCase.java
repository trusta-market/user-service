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
    UserResult signUp(SignUpCommand command);

    UserResult createUser(CreateUserCommand command);

    UserResult getUserByKeycloakId(KeycloakId keycloakId);

    UserResult getUser(UUID userId);

    UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command);

    void withdrawUser(KeycloakId keycloakId);

    DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role);

    List<AddressResult> getAddressList(UUID userId);

    AddressResult createAddress(CreateAddressCommand command);

    AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command);

    void deleteAddress(UUID userId, UUID addressId);

    void changeAddressDefault(UUID userId, UUID addressId);

    UserInternalResult getInternalUser(UUID userId);

    List<UserInternalResult> getInternalUserList(List<UUID> userIds);

    // 관리자: 유저 역할 변경
    UserResult changeUserRole(UUID userId, Role role);
}
