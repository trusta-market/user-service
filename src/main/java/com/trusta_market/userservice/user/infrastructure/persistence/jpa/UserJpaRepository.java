package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, UserId> {

    Optional<User> findByKeycloakIdAndDeletedAtIsNull(KeycloakId keycloakId);

    Optional<User> findByEmailAndDeletedAtIsNull(Email email);

    Optional<User> findByNameAndDeletedAtIsNull(Name name);

    boolean existsByEmailAndDeletedAtIsNull(Email email);

    boolean existsByNameAndDeletedAtIsNull(Name name);

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    Page<User> findAllByDeletedAtIsNullAndUserStatus(Pageable pageable, UserStatus userStatus);

    Page<User> findAllByDeletedAtIsNullAndRole(Pageable pageable, Role role);

    Page<User> findAllByDeletedAtIsNullAndUserStatusAndRole(Pageable pageable, UserStatus userStatus, Role role);

    List<User> findAllByUserIdInAndDeletedAtIsNull(Collection<UserId> userIds);
}
