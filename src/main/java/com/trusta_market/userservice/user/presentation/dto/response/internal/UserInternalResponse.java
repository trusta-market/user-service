package com.trusta_market.userservice.user.presentation.dto.response.internal;

import com.trusta_market.userservice.user.application.dto.result.internal.UserInternalResult;
import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;

import java.util.UUID;

public record UserInternalResponse(
        UUID userId,
        String email,
        String name,
        Role role,
        UserStatus userStatus,
        Membership membership
) {
    public static UserInternalResponse from(UserInternalResult result) {
        return new UserInternalResponse(
                result.userId(),
                result.email(),
                result.username(),
                result.role(),
                result.userStatus(),
                result.membership()
        );
    }
}
