package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.domain.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.trusta_market.userservice.user.domain.entity.QUser.user;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, JPAQueryFactory queryFactory) {
        this.userJpaRepository = userJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return userJpaRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public Optional<User> findByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public Optional<User> findByNicknameAndDeletedAtIsNull(String nickname) {
        return userJpaRepository.findByNicknameAndDeletedAtIsNull(nickname);
    }

    @Override
    public boolean existsByEmailAndDeletedAtIsNull(String email) {
        return userJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public boolean existsByNicknameAndDeletedAtIsNull(String nickname) {
        return userJpaRepository.existsByNicknameAndDeletedAtIsNull(nickname);
    }

    @Override
    public Page<User> findAllByDeletedAtIsNull(Pageable pageable) {
        return userJpaRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Override
    public Page<User> findAllByDeletedAtIsNullAndUserStatus(Pageable pageable, UserStatus userStatus) {
        return userJpaRepository.findAllByDeletedAtIsNullAndUserStatus(pageable, userStatus);
    }

    @Override
    public Page<User> findAllByDeletedAtIsNullAndRole(Pageable pageable, Role role) {
        return userJpaRepository.findAllByDeletedAtIsNullAndRole(pageable, role);
    }

    @Override
    public Page<User> findAllByDeletedAtIsNullAndUserStatusAndRole(Pageable pageable, UserStatus userStatus, Role role) {
        return userJpaRepository.findAllByDeletedAtIsNullAndUserStatusAndRole(pageable, userStatus, role);
    }

    @Override
    public List<User> findAllByUserIdInAndDeletedAtIsNull(Collection<UUID> userIds) {
        return userJpaRepository.findAllByUserIdInAndDeletedAtIsNull(userIds);
    }
}
