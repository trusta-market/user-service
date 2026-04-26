package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByNicknameAndDeletedAtIsNull(String nickname);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    boolean existsByNicknameAndDeletedAtIsNull(String nickname);

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    Page<User> findAllByDeletedAtIsNullAndUserStatus(Pageable pageable, UserStatus userStatus);

    Page<User> findAllByDeletedAtIsNullAndRole(Pageable pageable, Role role);

    Page<User> findAllByDeletedAtIsNullAndUserStatusAndRole(Pageable pageable, UserStatus userStatus, Role role);

    List<User> findAllByUserIdInAndDeletedAtIsNull(Collection<UUID> userIds);
}
