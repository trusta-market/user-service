package com.trusta_market.userservice.user.infrastructure.persistence.jpa;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.domain.repository.UserRepository;
import com.trusta_market.userservice.user.domain.pagination.DomainPage;
import com.trusta_market.userservice.user.domain.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Nickname;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Optional<User> findById(UserId userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return userJpaRepository.findByKeycloakIdAndDeletedAtIsNull(keycloakId);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public Optional<User> findByNickname(Nickname nickname) {
        return userJpaRepository.findByNicknameAndDeletedAtIsNull(nickname);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmailAndDeletedAtIsNull(email);
    }

    @Override
    public boolean existsByNickname(Nickname nickname) {
        return userJpaRepository.existsByNicknameAndDeletedAtIsNull(nickname);
    }

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

    @Override
    public List<User> findAllActiveUsersByIds(Collection<UserId> userIds) {
        return userJpaRepository.findAllByUserIdInAndDeletedAtIsNull(userIds);
    }
}
