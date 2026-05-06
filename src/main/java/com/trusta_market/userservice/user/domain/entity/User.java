package com.trusta_market.userservice.user.domain.entity;

import com.trustamarket.common.domain.BaseUserEntity;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.common.exception.DomainException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.EmailConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.NameConverter;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.KeycloakIdConverter;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.UserIdConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Convert(converter = KeycloakIdConverter.class)
    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private KeycloakId keycloakId;

    @Convert(converter = EmailConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private Email email;

    @Convert(converter = NameConverter.class)
    @Column(nullable = false, unique = true, length = 100)
    private Name name;

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
                KeycloakId keycloakId,
                Email email,
                Name name,
                Role role,
                UserStatus userStatus,
                Membership membership,
                String slackId,
                Integer version) {
        this.userId = userId != null ? userId.value() : null;
        this.keycloakId = keycloakId;
        this.email = email;
        this.name = name;
        this.role = role != null ? role : Role.MEMBER;
        this.userStatus = userStatus != null ? userStatus : UserStatus.APPROVED;
        this.membership = membership != null ? membership : Membership.BRONZE;
        this.slackId = slackId;
        this.version = version;
    }

    public static User create(KeycloakId keycloakId, Email email, Name name) {
        return User.builder()
                .userId(UserId.of(UUID.randomUUID()))
                .keycloakId(keycloakId)
                .email(email)
                .name(name)
                .build();
    }

    public UserId getUserId() {
        return userId != null ? UserId.of(userId) : null;
    }

    public void updateProfile(Name name) {
        if (name != null) {
            this.name = name;
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
            throw new DomainException(UserErrorCode.INVALID_STATUS_TRANSITION);
        }
        this.userStatus = UserStatus.APPROVED;
    }

    public void reject() {
        checkNotWithdrawn();
        if (this.userStatus != UserStatus.PENDING) {
            throw new DomainException(UserErrorCode.INVALID_STATUS_TRANSITION);
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
