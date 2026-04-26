package com.trusta_market.userservice.user.domain.repository;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;

import java.util.List;
import java.util.UUID;

public interface UserStatusHistoryRepository {

    UserStatusHistory save(UserStatusHistory history);

    List<UserStatusHistory> findAllByUserId(UUID userId);
}
