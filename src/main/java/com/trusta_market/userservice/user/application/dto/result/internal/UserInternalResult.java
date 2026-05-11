package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.user.domain.vo.Membership;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;

import java.util.UUID;

public record UserInternalResult(
        UUID userId,
        String username,
        String email,
        Role role,
        UserStatus userStatus,
        Membership membership
) {
    // 내부 조회용 사용자 결과로 변환한다.
    public static UserInternalResult from(com.trusta_market.userservice.user.domain.entity.User user) {
        return new UserInternalResult(
                user.getUserId().value(),
                user.getName().value(),
                user.getEmail().value(),
                user.getRole(),
                user.getUserStatus(),
                user.getMembership()
        );
    }
}
