package com.trusta_market.userservice.user.domain.repository;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.domain.pagination.DomainPage;
import com.trusta_market.userservice.user.domain.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Nickname;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    // 유저 저장
    User save(User user);

    // 식별자로 유저 조회
    Optional<User> findById(UserId userId);

    // Keycloak ID로 유저 조회
    Optional<User> findByKeycloakId(String keycloakId);

    // 이메일로 유저 조회
    Optional<User> findByEmail(Email email);

    // 닉네임으로 유저 조회
    Optional<User> findByNickname(Nickname nickname);

    // 이메일 중복 확인
    boolean existsByEmail(Email email);

    // 닉네임 중복 확인
    boolean existsByNickname(Nickname nickname);

    // 활성 유저 페이징 조회
    DomainPage<User> findAllActiveUsers(DomainPageRequest pageRequest);

    // 상태별 활성 유저 페이징 조회
    DomainPage<User> findAllActiveUsersByStatus(DomainPageRequest pageRequest, UserStatus userStatus);

    // 역할별 활성 유저 페이징 조회
    DomainPage<User> findAllActiveUsersByRole(DomainPageRequest pageRequest, Role role);

    // 상태 및 역할별 활성 유저 페이징 조회
    DomainPage<User> findAllActiveUsersByStatusAndRole(DomainPageRequest pageRequest, UserStatus userStatus, Role role);

    // 다중 식별자로 활성 유저 목록 조회
    List<User> findAllActiveUsersByIds(Collection<UserId> userIds);
}
