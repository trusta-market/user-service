package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;

import java.util.UUID;

public record UserInternalResult(
        UUID userId,
        String email,
        String name,
        Role role,
        UserStatus userStatus,
        Membership membership
) {
    public static UserInternalResult from(User user) {
        return new UserInternalResult(
                user.getUserId().value(),
                user.getEmail().value(),
                user.getName().value(),
                user.getRole(),
                user.getUserStatus(),
                user.getMembership()
        );
    }
}
