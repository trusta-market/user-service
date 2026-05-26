package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 유저 엔티티 Spring Data JPA 인터페이스 (소프트 삭제 및 비관적 락 지원)
public interface UserJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserIdAndDeletedAtIsNull(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.userId = :userId and u.deletedAt is null")
    Optional<User> findByIdWithLock(@Param("userId") UUID userId);

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
