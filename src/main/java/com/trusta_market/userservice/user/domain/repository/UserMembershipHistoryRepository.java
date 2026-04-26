package com.trusta_market.userservice.user.domain.repository;

import com.trusta_market.userservice.user.domain.entity.UserMembershipHistory;

import java.util.List;
import java.util.UUID;

public interface UserMembershipHistoryRepository {

    UserMembershipHistory save(UserMembershipHistory history);

    List<UserMembershipHistory> findAllByUserId(UUID userId);
}
