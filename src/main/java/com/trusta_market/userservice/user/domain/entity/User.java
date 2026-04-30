package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseUserEntity;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Nickname;
import com.trusta_market.userservice.user.domain.vo.Realname;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.domain.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.UserIdConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.EmailConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.NicknameConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.RealnameConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_users")
public class User extends BaseUserEntity {

    @Id
    @Convert(converter = UserIdConverter.class)
    @Column(nullable = false, updatable = false)
    private UserId userId;

    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String keycloakId;

    @Convert(converter = EmailConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private Email email;

    @Convert(converter = RealnameConverter.class)
    @Column(nullable = false, length = 100)
    private Realname realname;

    @Convert(converter = NicknameConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private Nickname nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Membership membership;

    @Column(length = 100)
    private String slackId;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public User(UserId userId,
                String keycloakId,
                Email email,
                Realname realname,
                Nickname nickname,
                Role role,
                UserStatus userStatus,
                Membership membership,
                String slackId,
                Integer version) {
        this.userId = userId;
        this.keycloakId = keycloakId;
        this.email = email;
        this.realname = realname;
        this.nickname = nickname;
        this.role = role != null ? role : Role.MEMBER;
        this.userStatus = userStatus != null ? userStatus : UserStatus.PENDING;
        this.membership = membership != null ? membership : Membership.BRONZE;
        this.slackId = slackId;
        this.version = version;
    }

    public static User create(String keycloakId, Email email, Realname realname, Nickname nickname) {
        return User.builder()
                .userId(UserId.of(UUID.randomUUID()))
                .keycloakId(keycloakId)
                .email(email)
                .realname(realname)
                .nickname(nickname)
                .build();
    }

    public void updateProfile(Nickname nickname, Realname realname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (realname != null) {
            this.realname = realname;
        }
    }

    public void withdraw(UUID userId) {
        if (isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
        super.delete(userId);
    }

    public void approve() {
        checkNotWithdrawn();
        if (this.userStatus != UserStatus.PENDING) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN); // TODO: 적절한 에러 코드로 변경 필요
        }
        this.userStatus = UserStatus.APPROVED;
    }

    public void reject() {
        checkNotWithdrawn();
        if (this.userStatus != UserStatus.PENDING) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN); // TODO: 적절한 에러 코드로 변경 필요
        }
        this.userStatus = UserStatus.REJECTED;
    }

    public void suspend() {
        checkNotWithdrawn();
        if (this.userStatus == UserStatus.SUSPENDED) {
            throw new DomainException(UserErrorCode.SUSPENDED_USER);
        }
        this.userStatus = UserStatus.SUSPENDED;
    }

    public void unsuspend() {
        checkNotWithdrawn();
        if (this.userStatus != UserStatus.SUSPENDED) {
            throw new DomainException(UserErrorCode.USER_NOT_FOUND);
        }
        this.userStatus = UserStatus.APPROVED;
    }

    private void checkNotWithdrawn() {
        if (isDeleted()) {
            throw new DomainException(UserErrorCode.ALREADY_WITHDRAWN);
        }
    }
}
