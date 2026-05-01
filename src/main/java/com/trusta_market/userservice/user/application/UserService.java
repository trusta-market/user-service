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

// 유저 도메인 비즈니스 로직 구현 서비스
@Service
@Transactional
public class UserService implements UserUseCase, UserValidationUseCase {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public UserService(UserRepository userRepository, UserAddressRepository userAddressRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
    }

    // 신규 유저 생성 (이메일/이름 중복 검사 포함)
    @Override
    public UserResult createUser(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new DomainException(UserErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByName(command.name())) {
            throw new DomainException(UserErrorCode.INVALID_NAME);
        }

        User savedUser = userRepository.save(User.create(command.keycloakId(), command.email(), command.name()));
        return UserResult.from(savedUser);
    }

    // Keycloak ID를 통한 유저 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserResult getUserByKeycloakId(KeycloakId keycloakId) {
        return UserResult.from(findUserByKeycloakId(keycloakId));
    }

    // 시스템 내부 ID(UUID)를 통한 유저 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserResult getUser(UUID userId) {
        return UserResult.from(findUserById(userId));
    }

    // 유저 프로필(이름) 업데이트
    @Override
    public UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command) {
        User user = findUserByKeycloakId(keycloakId);
        assertUserCanMutate(user); // 변경 가능 상태인지 검증
        if (command.name() != null && !command.name().equals(user.getName())
                && userRepository.existsByName(command.name())) {
            throw new DomainException(UserErrorCode.INVALID_NAME);
        }
        user.updateProfile(command.name());
        return UserResult.from(userRepository.save(user));
    }

    // 회원 탈퇴 (Soft Delete)
    @Override
    public void withdrawUser(KeycloakId keycloakId) {
        User user = findUserByKeycloakId(keycloakId);
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        user.withdraw(user.getUserId().value());
        userRepository.save(user);
    }

    // 유저의 활성 배송지 목록 조회 (생성일 순 정렬)
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

    // 신규 배송지 등록 (최대 10개 제한)
    @Override
    public AddressResult createAddress(CreateAddressCommand command) {
        User user = findUserById(command.userId());
        assertUserCanMutate(user);
        if (userAddressRepository.countActiveAddressesByUserId(UserId.of(command.userId())) >= 10) {
            throw new DomainException(AddressErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }

        // 첫 번째 배송지인 경우 자동으로 대표 배송지 설정
        boolean makeDefault = userAddressRepository.countActiveAddressesByUserId(UserId.of(command.userId())) == 0;
        return AddressResult.from(userAddressRepository.save(
                UserAddress.create(UserId.of(command.userId()), command.recipientName(), command.recipientPhone(),
                        command.zipCode(), command.address(), command.addressDetail(), makeDefault)));
    }

    // 배송지 정보 수정
    @Override
    public AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        address.update(command.recipientName(), command.recipientPhone(), command.zipCode(), command.address(),
                command.addressDetail());
        return AddressResult.from(userAddressRepository.save(address));
    }

    // 배송지 삭제 (대표 배송지 삭제 시 다른 배송지를 대표로 위임)
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

    // 대표 배송지 설정 변경
    @Override
    public void changeAddressDefault(UUID userId, UUID addressId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        userAddressRepository.clearDefaultAddress(UserId.of(userId)); // 기존 대표 설정 해제
        address.markAsDefaultAddress();
        userAddressRepository.save(address);
    }

    // 유저 목록 페이징 조회 (관리자용 필터링 포함)
    @Override
    @Transactional(readOnly = true)
    public DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role) {
        DomainPageRequest pageRequest = DomainPageRequest.of(page, size);
        DomainPage<User> users;
        if (userStatus != null && role != null) {
            users = userRepository.findAllActiveUsersByStatusAndRole(pageRequest, userStatus, role);
        } else if (userStatus != null) {
            users = userRepository.findAllActiveUsersByStatus(pageRequest, userStatus);
        } else if (role != null) {
            users = userRepository.findAllActiveUsersByRole(pageRequest, role);
        } else {
            users = userRepository.findAllActiveUsers(pageRequest);
        }
        return users.map(UserResult::from);
    }

    // 관리자: 유저 가입 승인
    @Override
    public UserResult approveUser(UUID userId) {
        User user = findUserById(userId);
        user.approve();
        return UserResult.from(userRepository.save(user));
    }

    // 관리자: 유저 가입 거절
    @Override
    public UserResult rejectUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.reject();
        return UserResult.from(userRepository.save(user));
    }

    // 관리자: 유저 정지
    @Override
    public UserResult suspendUser(UUID userId, String reason, LocalDateTime expiresAt) {
        User user = findUserById(userId);
        user.suspend();
        return UserResult.from(userRepository.save(user));
    }

    // 관리자: 유저 정지 해제
    @Override
    public UserResult unsuspendUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.unsuspend();
        return UserResult.from(userRepository.save(user));
    }

    // 타 서비스용: 유저 내부 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserInternalResult getInternalUser(UUID userId) {
        return UserInternalResult.from(findUserById(userId));
    }

    // 타 서비스용: 대량 유저 정보 조회
    @Override
    @Transactional(readOnly = true)
    public List<UserInternalResult> getInternalUserList(List<UUID> userIds) {
        List<UserId> voIds = userIds.stream().map(UserId::of).toList();
        return userRepository.findAllActiveUsersByIds(voIds).stream()
                .map(UserInternalResult::from)
                .toList();
    }

    // 타 서비스용: 유저 활성 상태 검증
    @Override
    public void validateInternalUser(UUID userId) {
        User user = findUserById(userId);
        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new DomainException(InternalErrorCode.USER_NOT_ACTIVE);
        }
    }

    // 유저 멤버십 정보 조회
    @Override
    @Transactional(readOnly = true)
    public MembershipResult getMembership(UUID userId) {
        return MembershipResult.from(findUserById(userId));
    }

    // Keycloak ID 기반 유저 검색 헬퍼
    private User findUserByKeycloakId(KeycloakId keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }

    // UserId 기반 유저 검색 헬퍼
    private User findUserById(UUID userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        return user;
    }

    // 유저 정보 변경 가능 여부(상태) 검사 헬퍼
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

    // 배송지 소유권 확인 및 검색 헬퍼
    private UserAddress findOwnedAddress(UUID userId, UUID addressId) {
        return userAddressRepository.findActiveAddressByIdAndUserId(AddressId.of(addressId), UserId.of(userId))
                .orElseThrow(() -> new DomainException(AddressErrorCode.ADDRESS_NOT_FOUND));
    }

    // === UserValidationUseCase 구현 ===

    // 유저가 활성 상태(APPROVED)인지 검증
    @Override
    @Transactional(readOnly = true)
    public void validateActiveUser(UUID userId) {
        validateInternalUser(userId);
    }

    // 유저가 데이터 변경이 가능한 상태인지 검증
    @Override
    @Transactional(readOnly = true)
    public void validateUserCanMutate(UUID userId) {
        assertUserCanMutate(findUserById(userId));
    }
}
