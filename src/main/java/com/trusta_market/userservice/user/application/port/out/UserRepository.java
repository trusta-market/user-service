package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.common.pagination.DomainPageRequest;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId userId);
    Optional<User> findByIdWithLock(UserId userId);
    Optional<User> findByKeycloakId(KeycloakId keycloakId);
    Optional<User> findByEmail(Email email);
    Optional<User> findByName(Name name);
    boolean existsByEmail(Email email);
    boolean existsByName(Name name);
    DomainPage<User> findAllActiveUsers(DomainPageRequest pageRequest);
    DomainPage<User> findAllActiveUsersByStatus(DomainPageRequest pageRequest, UserStatus userStatus);
    DomainPage<User> findAllActiveUsersByRole(DomainPageRequest pageRequest, Role role);
    DomainPage<User> findAllActiveUsersByStatusAndRole(DomainPageRequest pageRequest, UserStatus userStatus, Role role);
    List<User> findAllActiveUsersByIds(Collection<UserId> userIds);
}
