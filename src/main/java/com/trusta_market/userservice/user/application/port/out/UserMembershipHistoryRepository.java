package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.entity.UserMembershipHistory;

public interface UserMembershipHistoryRepository {
    UserMembershipHistory save(UserMembershipHistory history);
}
