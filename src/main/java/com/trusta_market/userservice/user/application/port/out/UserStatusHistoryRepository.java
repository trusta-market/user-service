package com.trusta_market.userservice.user.application.port.out;

import com.trusta_market.userservice.user.domain.entity.UserStatusHistory;

public interface UserStatusHistoryRepository {
    UserStatusHistory save(UserStatusHistory history);
}
