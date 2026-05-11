package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipResult(
        UUID userId,
        Membership membership
) {
    public static MembershipResult from(User user) {
        return new MembershipResult(
                user.getUserId().value(),
                user.getMembership()
        );
    }
}
