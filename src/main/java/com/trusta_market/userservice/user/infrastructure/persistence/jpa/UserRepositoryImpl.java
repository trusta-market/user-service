package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.application.port.out.UserRepository;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// UserRepository 인터페이스의 JPA/Querydsl 기반 구현체 (Adapter)
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, JPAQueryFactory queryFactory) {
        this.userJpaRepository = userJpaRepository;
        this.queryFactory = queryFactory;
    }

    // 유저 엔티티 정보 저장 또는 업데이트
    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    // 내부 식별자(UserId)를 통해 삭제되지 않은 유저 조회
    @Override
    public Optional<User> findById(UserId userId) {
        return userJpaRepository.findByUserIdAndDeletedAtIsNull(userId.value());
    }

    // 내부 식별자(UserId)를 통해 비관적 락(PESSIMISTIC_WRITE)을 걸어 유저 조회
    @Override
    public Optional<User> findByIdWithLock(UserId userId) {
        return userJpaRepository.findByIdWithLock(userId.value());
    }

    // Keycloak 식별자를 통해 유저 정보 조회
    @Override
    public Optional<User> findByKeycloakId(KeycloakId keycloakId) {
        return userJpaRepository.findByKeycloakIdAndDeletedAtIsNull(keycloakId);
    }

    // 이메일 VO를 통해 유저 정보 조회
    @Override
    public Optional<User> findByEmail(Email email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email);
    }

    // 이름(닉네임) VO를 통해 유저 정보 조회
    @Override
    public Optional<User> findByName(Name name) {
        return userJpaRepository.findByNameAndDeletedAtIsNull(name);
    }

    // 동일한 이메일을 가진 유저 존재 여부 확인
    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    // 동일한 이름(닉네임)을 가진 유저 존재 여부 확인
    @Override
    public boolean existsByName(Name name) {
        return userJpaRepository.existsByNameAndDeletedAtIsNull(name);
    }

    // 활성화된 전체 유저 목록 페이징 조회
    @Override
    public DomainPage<User> findAllActiveUsers(DomainPageRequest pageRequest) {
        Page<User> page = userJpaRepository.findAllByDeletedAtIsNull(PageRequest.of(pageRequest.page(), pageRequest.size()));
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public DomainPage<User> findAllActiveUsersByStatus(DomainPageRequest pageRequest, UserStatus userStatus) {
        Page<User> page = userJpaRepository.findAllByDeletedAtIsNullAndUserStatus(PageRequest.of(pageRequest.page(), pageRequest.size()), userStatus);
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public DomainPage<User> findAllActiveUsersByRole(DomainPageRequest pageRequest, Role role) {
        Page<User> page = userJpaRepository.findAllByDeletedAtIsNullAndRole(PageRequest.of(pageRequest.page(), pageRequest.size()), role);
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public DomainPage<User> findAllActiveUsersByStatusAndRole(DomainPageRequest pageRequest, UserStatus userStatus, Role role) {
        Page<User> page = userJpaRepository.findAllByDeletedAtIsNullAndUserStatusAndRole(PageRequest.of(pageRequest.page(), pageRequest.size()), userStatus, role);
        return DomainPage.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    // 여러 개의 내부 식별자(UserId) 리스트를 통해 유저 목록 일괄 조회
    @Override
    public List<User> findAllActiveUsersByIds(Collection<UserId> userIds) {
        return userJpaRepository.findAllByUserIdInAndDeletedAtIsNull(userIds);
    }
}
