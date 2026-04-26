package com.trusta_market.userservice.user.domain.entity;

import com.trusta_market.userservice.common.domain.entity.BaseUserEntity;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Nickname;
import com.trusta_market.userservice.user.domain.vo.Realname;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_users")
public class User extends BaseUserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String keycloakId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String realname;

    @Column(nullable = false, unique = true, length = 100)
    private String nickname;

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
    public User(UUID userId,
                String keycloakId,
                String email,
                String realname,
                String nickname,
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
                .keycloakId(keycloakId)
                .email(email.value())
                .realname(realname.value())
                .nickname(nickname.value())
                .build();
    }

    public void updateProfile(Nickname nickname, Realname realname) {
        if (nickname != null) {
            this.nickname = nickname.value();
        }
        if (realname != null) {
            this.realname = realname.value();
        }
    }

    public void approve() {
        this.userStatus = UserStatus.APPROVED;
    }

    public void reject() {
        this.userStatus = UserStatus.REJECTED;
    }

    public void suspend() {
        this.userStatus = UserStatus.SUSPENDED;
    }

    public void unsuspend() {
        this.userStatus = UserStatus.APPROVED;
    }
}
