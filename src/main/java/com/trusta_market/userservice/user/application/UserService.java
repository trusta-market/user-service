package com.trusta_market.userservice.user.application;

import com.trusta_market.userservice.account.domain.UserAccount;
import com.trusta_market.userservice.account.domain.repository.UserAccountRepository;
import com.trusta_market.userservice.report.domain.UserReport;
import com.trusta_market.userservice.report.domain.repository.UserReportRepository;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.trustscore.domain.TrustScore;
import com.trusta_market.userservice.trustscore.domain.repository.TrustScoreRepository;
import com.trusta_market.userservice.user.application.dto.command.CreateAccountCommand;
import com.trusta_market.userservice.user.application.dto.command.CreateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.CreateReportCommand;
import com.trusta_market.userservice.user.application.dto.command.CreateUserCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateUserCommand;
import com.trusta_market.userservice.user.application.dto.result.AccountResult;
import com.trusta_market.userservice.user.application.dto.result.AddressResult;
import com.trusta_market.userservice.user.application.dto.result.ReportResult;
import com.trusta_market.userservice.user.application.dto.result.UserResult;
import com.trusta_market.userservice.user.application.dto.result.internal.AccountInternalResult;
import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;
import com.trusta_market.userservice.user.application.dto.result.internal.TrustScoreResult;
import com.trusta_market.userservice.user.application.dto.result.internal.UserInternalResult;
import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.entity.UserAddress;
import com.trusta_market.userservice.user.domain.pagination.DomainPage;
import com.trusta_market.userservice.user.domain.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.domain.repository.UserAddressRepository;
import com.trusta_market.userservice.user.domain.repository.UserRepository;
import com.trusta_market.userservice.user.domain.vo.AddressId;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Nickname;
import com.trusta_market.userservice.user.domain.vo.Realname;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.domain.exception.AccountErrorCode;
import com.trusta_market.userservice.user.domain.exception.AddressErrorCode;
import com.trusta_market.userservice.user.domain.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.InternalErrorCode;
import com.trusta_market.userservice.user.domain.exception.ReportErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserReportRepository userReportRepository;
    private final TrustScoreRepository trustScoreRepository;

    public UserService(UserRepository userRepository,
            UserAddressRepository userAddressRepository,
            UserAccountRepository userAccountRepository,
            UserReportRepository userReportRepository,
            TrustScoreRepository trustScoreRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.userAccountRepository = userAccountRepository;
        this.userReportRepository = userReportRepository;
        this.trustScoreRepository = trustScoreRepository;
    }

    public UserResult createUser(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new DomainException(UserErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByNickname(command.nickname())) {
            throw new DomainException(UserErrorCode.DUPLICATE_NICKNAME);
        }

        User savedUser = userRepository.save(
                User.create(command.keycloakId(), command.email(), command.realName(), command.nickname()));
        trustScoreRepository.save(TrustScore.create(savedUser.getUserId()));
        return UserResult.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResult getUserByKeycloakId(KeycloakId keycloakId) {
        return UserResult.from(findUserByKeycloakId(keycloakId));
    }

    @Transactional(readOnly = true)
    public UserResult getUser(UUID userId) {
        return UserResult.from(findUserById(userId));
    }

    public UserResult updateUser(KeycloakId keycloakId, UpdateUserCommand command) {
        User user = findUserByKeycloakId(keycloakId);
        assertUserCanMutate(user);
        if (command.nickname() != null && !command.nickname().equals(user.getNickname())
                && userRepository.existsByNickname(command.nickname())) {
            throw new DomainException(UserErrorCode.DUPLICATE_NICKNAME);
        }
        user.updateProfile(command.nickname(), command.realName());
        return UserResult.from(userRepository.save(user));
    }

    public void withdrawUser(KeycloakId keycloakId) {
        User user = findUserByKeycloakId(keycloakId);
        if (user.isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        user.delete(user.getUserId().value()); // Self deletion
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<AddressResult> getAddressList(UUID userId) {
        assertUserCanMutate(findUserById(userId));
        return userAddressRepository.findAllActiveAddressesByUserId(UserId.of(userId))
                .stream()
                .sorted(Comparator.comparing(UserAddress::getCreatedAt))
                .map(AddressResult::from)
                .toList();
    }

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

    public AddressResult updateAddress(UUID userId, UUID addressId, UpdateAddressCommand command) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        address.update(command.recipientName(), command.recipientPhone(), command.zipCode(), command.address(),
                command.addressDetail());
        return AddressResult.from(userAddressRepository.save(address));
    }

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

    public void changeAddressDefault(UUID userId, UUID addressId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAddress address = findOwnedAddress(userId, addressId);
        userAddressRepository.clearDefaultAddress(UserId.of(userId));
        address.markAsDefaultAddress();
        userAddressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public List<AccountResult> getAccountList(UUID userId) {
        assertUserCanMutate(findUserById(userId));
        return userAccountRepository.findAllActiveAccountsByUserId(UserId.of(userId))
                .stream()
                .sorted(Comparator.comparing(UserAccount::getCreatedAt))
                .map(AccountResult::from)
                .toList();
    }

    public AccountResult createAccount(CreateAccountCommand command) {
        User user = findUserById(command.userId());
        assertUserCanMutate(user);
        if (userAccountRepository.countActiveAccountsByUserId(UserId.of(command.userId())) >= 5) {
            throw new DomainException(AccountErrorCode.ACCOUNT_LIMIT_EXCEEDED);
        }

        boolean makeDefault = userAccountRepository.countActiveAccountsByUserId(UserId.of(command.userId())) == 0;
        return AccountResult.from(userAccountRepository.save(
                UserAccount.create(UserId.of(command.userId()), command.bankCode(), command.accountNumber(),
                        command.accountHolder(), command.accountType(), false, makeDefault)));
    }

    public void deleteAccount(UUID userId, UUID accountId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAccount account = findOwnedAccount(userId, accountId);
        boolean wasDefault = account.isDefault();
        account.delete(userId);
        userAccountRepository.save(account);
        if (wasDefault) {
            userAccountRepository.findAllActiveAccountsByUserId(UserId.of(userId)).stream()
                    .filter(UserAccount::isVerified)
                    .findFirst()
                    .ifPresent(next -> {
                        next.setDefault(true);
                        userAccountRepository.save(next);
                    });
        }
    }

    public void changeAccountDefault(UUID userId, UUID accountId) {
        User user = findUserById(userId);
        assertUserCanMutate(user);
        UserAccount account = findOwnedAccount(userId, accountId);
        if (!account.isVerified()) {
            throw new DomainException(AccountErrorCode.ACCOUNT_NOT_VERIFIED);
        }
        userAccountRepository.clearDefaultAccount(UserId.of(userId));
        account.setDefault(true);
        userAccountRepository.save(account);
    }

    public ReportResult createReport(CreateReportCommand command) {
        assertUserCanMutate(findUserById(command.reporterUserId()));
        if (command.reporterUserId().equals(command.reportedUserId())) {
            throw new DomainException(ReportErrorCode.SELF_REPORT);
        }
        findUserById(command.reporterUserId());
        findUserById(command.reportedUserId());
        if (userReportRepository.existsByReporterUserIdAndReportedUserId(UserId.of(command.reporterUserId()),
                UserId.of(command.reportedUserId()))) {
            throw new DomainException(ReportErrorCode.DUPLICATE_REPORT);
        }

        return ReportResult.from(userReportRepository.save(
                UserReport.create(UserId.of(command.reporterUserId()), UserId.of(command.reportedUserId()), command.reason())));
    }

    @Transactional(readOnly = true)
    public List<ReportResult> getReportList(UUID reporterUserId) {
        assertUserCanMutate(findUserById(reporterUserId));
        return userReportRepository.findAllByReporterUserId(UserId.of(reporterUserId))
                .stream()
                .sorted(Comparator.comparing(UserReport::getCreatedAt))
                .map(ReportResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DomainPage<UserResult> getUserPage(int page, int size, UserStatus userStatus, Role role) {
        DomainPage<User> users = pageUsers(page, size, userStatus, role);
        return users.map(UserResult::from);
    }

    public UserResult approveUser(UUID userId) {
        User user = findUserById(userId);
        user.approve();
        return UserResult.from(userRepository.save(user));
    }

    public UserResult rejectUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.reject();
        return UserResult.from(userRepository.save(user));
    }

    public UserResult suspendUser(UUID userId, String reason, LocalDateTime expiresAt) {
        User user = findUserById(userId);
        user.suspend();
        return UserResult.from(userRepository.save(user));
    }

    public UserResult unsuspendUser(UUID userId, String reason) {
        User user = findUserById(userId);
        user.unsuspend();
        return UserResult.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public DomainPage<ReportResult> getReportPage(int page, int size, ReportStatus status) {
        DomainPageRequest pageRequest = DomainPageRequest.of(page, size);
        DomainPage<UserReport> reports = status == null
                ? userReportRepository.findAll(pageRequest)
                : userReportRepository.findAllByStatus(pageRequest, status);
        return reports.map(ReportResult::from);
    }

    public void reviewReport(UUID reportId, String adminMemo) {
        UserReport report = findReportById(reportId);
        report.review(UUID.randomUUID()); // Mock admin ID
        userReportRepository.save(report);
    }

    public void dismissReport(UUID reportId, String adminMemo) {
        UserReport report = findReportById(reportId);
        report.dismiss(UUID.randomUUID()); // Mock admin ID
        userReportRepository.save(report);
    }

    public void verifyAccount(UUID accountId) {
        UserAccount account = findAccountById(accountId);
        account.approve(UUID.randomUUID()); // Mock admin ID
        userAccountRepository.save(account);
    }

    public void rejectAccount(UUID accountId, String reason) {
        UserAccount account = findAccountById(accountId);
        // Implement reject logic if needed, currently approve is implemented
        userAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public UserInternalResult getInternalUser(UUID userId) {
        User user = findUserById(userId);
        return UserInternalResult.from(user);
    }

    @Transactional(readOnly = true)
    public List<UserInternalResult> getInternalUserList(List<UUID> userIds) {
        List<UserId> voIds = userIds.stream().map(UserId::of).toList();
        return userRepository.findAllActiveUsersByIds(voIds).stream()
                .map(UserInternalResult::from)
                .toList();
    }

    public void validateInternalUser(UUID userId) {
        User user = findUserById(userId);
        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new DomainException(InternalErrorCode.USER_NOT_ACTIVE);
        }
    }

    @Transactional(readOnly = true)
    public TrustScoreResult getTrustScore(UUID userId) {
        findUserById(userId);
        TrustScore trustScore = trustScoreRepository.findByUserId(UserId.of(userId))
                .orElseGet(() -> trustScoreRepository.save(TrustScore.create(UserId.of(userId))));
        return TrustScoreResult.from(trustScore);
    }

    @Transactional(readOnly = true)
    public MembershipResult getMembership(UUID userId) {
        User user = findUserById(userId);
        return MembershipResult.from(user);
    }

    @Transactional(readOnly = true)
    public AccountInternalResult getDefaultAccount(UUID userId) {
        UserAccount account = userAccountRepository.findDefaultAccountByUserId(UserId.of(userId))
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
        if (!account.isVerified()) {
            throw new DomainException(AccountErrorCode.ACCOUNT_NOT_VERIFIED);
        }
        return AccountInternalResult.from(account);
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

    private UserAccount findOwnedAccount(UUID userId, UUID accountId) {
        return userAccountRepository.findActiveAccountByIdAndUserId(accountId, UserId.of(userId))
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
    }

    private UserAccount findAccountById(UUID accountId) {
        return userAccountRepository.findById(accountId)
                .orElseThrow(() -> new DomainException(AccountErrorCode.ACCOUNT_NOT_FOUND));
    }

    private UserReport findReportById(UUID reportId) {
        return userReportRepository.findById(reportId)
                .orElseThrow(() -> new DomainException(ReportErrorCode.REPORT_NOT_FOUND));
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
}
