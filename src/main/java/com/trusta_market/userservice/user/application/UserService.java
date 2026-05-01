package com.trusta_market.userservice.user.application;

import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.application.dto.command.CreateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.CreateUserCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateUserCommand;
import com.trusta_market.userservice.user.application.dto.result.AddressResult;
import com.trusta_market.userservice.user.application.dto.result.UserResult;
import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;
import com.trusta_market.userservice.user.application.dto.result.internal.UserInternalResult;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.application.port.in.UserValidationUseCase;
import com.trusta_market.userservice.user.application.port.out.UserAddressRepository;
import com.trusta_market.userservice.user.application.port.out.UserRepository;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.entity.UserAddress;
import com.trusta_market.userservice.user.domain.exception.AddressErrorCode;
import com.trusta_market.userservice.user.domain.exception.InternalErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserUseCase, UserValidationUseCase {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public UserService(UserRepository userRepository,
            UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public UserResult createUser(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new DomainException(UserErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByName(command.name())) {
            throw new DomainException(UserErrorCode.DUPLICATE_NICKNAME); // 명칭 통일 필요하나 일단 기존 에러코드 사용
        }

        User savedUser = userRepository.save(
                User.create(command.keycloakId(), command.email(), command.name()));
        return UserResult.from(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResult getUserByKeycloakId(KeycloakId keycloakId) {
        return UserResult.from(findUserByKeycloakId(keycloakId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResult getUser(UUID userId) {
        return UserResult.from(findUserById(userId));
    }

    @Override
    public UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command) {
        User user = findUserByKeycloakId(keycloakId);
        assertUserCanMutate(user);
        if (command.name() != null && !command.name().equals(user.getName())
                && userRepository.existsByName(command.name())) {
            throw new DomainException(UserErrorCode.DUPLICATE_NICKNAME);
        }
        user.updateProfile(command.name());
        return UserResult.from(userRepository.save(user));
    }

    @Override
    public void withdrawUser(KeycloakId keycloakId) {
        User user = findUserByKeycloakId(keycloakId);
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        user.withdraw(user.getUserId().value());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResult> getAddressList(UUID userId) {
        assertUserCanMutate(findUserById(userId));
        return userAddressRepository.findAllActiveAddressesByUserId(UserId.of(userId))
                .stream()
                .sorted(Comparator.comparing(UserAddress::getCreatedAt))
                .map(AddressResult::from)
                .toList();
    }

    @Override
    public AddressResult createAddress(CreateAddressCommand command) {
        User user = findUserById(command.userId());
        assertUserCanMutate(user);
        if (userAddressRepository.countActiveAddressesByUserId(UserId.of(command.userId())) >= 10) {
            throw new DomainException(AddressErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }

        boolean makeDefault = userAddressRepository.countActiveAddressesByUserId(UserId.of(command.userId())) == 0;
        return AddressResult.from(userAddressRepository.save(
                UserAddress.create(UserId.of(command.userId()), command.recipientName(), command.recipientPhone(),
                        command.zipCode(), command.address(), command.addressDetail(), makeDefault)));
    }

    @Override
    public AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        address.update(command.recipientName(), command.recipientPhone(), command.zipCode(), command.address(),
                command.addressDetail());
        return AddressResult.from(userAddressRepository.save(address));
    }

    @Override
    public void deleteAddress(UUID userId, UUID addressId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        boolean wasDefault = address.isDefault();
        address.delete(userId);
        userAddressRepository.save(address);
        if (wasDefault) {
            userAddressRepository.findAllActiveAddressesByUserId(UserId.of(userId)).stream()
                    .findFirst()
                    .ifPresent(next -> {
                        next.markAsDefaultAddress();
                        userAddressRepository.save(next);
                    });
        }
    }

    @Override
    public void changeAddressDefault(UUID userId, UUID addressId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        userAddressRepository.clearDefaultAddress(UserId.of(userId));
        address.markAsDefaultAddress();
        userAddressRepository.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role) {
        DomainPage<User> users = pageUsers(page, size, userStatus, role);
        return users.map(UserResult::from);
    }

    @Override
    public UserResult approveUser(UUID userId) {
        User user = findUserById(userId);
        user.approve();
        return UserResult.from(userRepository.save(user));
    }

    @Override
    public UserResult rejectUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.reject();
        return UserResult.from(userRepository.save(user));
    }

    @Override
    public UserResult suspendUser(UUID userId, String reason, LocalDateTime expiresAt) {
        User user = findUserById(userId);
        user.suspend();
        return UserResult.from(userRepository.save(user));
    }

    @Override
    public UserResult unsuspendUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.unsuspend();
        return UserResult.from(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserInternalResult getInternalUser(UUID userId) {
        User user = findUserById(userId);
        return UserInternalResult.from(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInternalResult> getInternalUserList(List<UUID> userIds) {
        List<UserId> voIds = userIds.stream().map(UserId::of).toList();
        return userRepository.findAllActiveUsersByIds(voIds).stream()
                .map(UserInternalResult::from)
                .toList();
    }

    @Override
    public void validateInternalUser(UUID userId) {
        User user = findUserById(userId);
        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new DomainException(InternalErrorCode.USER_NOT_ACTIVE);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipResult getMembership(UUID userId) {
        User user = findUserById(userId);
        return MembershipResult.from(user);
    }

    private User findUserByKeycloakId(KeycloakId keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }

    private User findUserById(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }

    private void assertUserCanMutate(User user) {
        if (user.getUserStatus() == UserStatus.PENDING) {
            throw new DomainException(UserErrorCode.PENDING_USER);
        }
        if (user.getUserStatus() == UserStatus.REJECTED) {
            throw new DomainException(UserErrorCode.REJECTED_USER);
        }
        if (user.getUserStatus() == UserStatus.SUSPENDED) {
            throw new DomainException(UserErrorCode.SUSPENDED_USER);
        }
    }

    private UserAddress findOwnedAddress(UUID userId, UUID addressId) {
        return userAddressRepository.findActiveAddressByIdAndUserId(AddressId.of(addressId), UserId.of(userId))
                .orElseThrow(() -> new DomainException(AddressErrorCode.ADDRESS_NOT_FOUND));
    }

    private DomainPage<User> pageUsers(int page, int size, UserStatus userStatus, Role role) {
        DomainPageRequest pageRequest = DomainPageRequest.of(page, size);
        if (userStatus != null && role != null) {
            return userRepository.findAllActiveUsersByStatusAndRole(pageRequest, userStatus, role);
        }
        if (userStatus != null) {
            return userRepository.findAllActiveUsersByStatus(pageRequest, userStatus);
        }
        if (role != null) {
            return userRepository.findAllActiveUsersByRole(pageRequest, role);
        }
        return userRepository.findAllActiveUsers(pageRequest);
    }

    // === UserValidationUseCase 구현 ===

    @Override
    @Transactional(readOnly = true)
    public void validateActiveUser(UUID userId) {
        validateInternalUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserCanMutate(UUID userId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
    }
}
