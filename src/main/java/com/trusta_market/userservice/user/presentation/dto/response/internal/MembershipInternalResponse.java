package com.trusta_market.userservice.user.presentation.dto.response.internal;

import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;
import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipInternalResponse(
        UUID userId,
        Membership membership,
        int rollingPoints
) {
    public static MembershipInternalResponse from(MembershipResult result) {
        return new MembershipInternalResponse(result.userId(), result.membership(), result.rollingPoints());
    }
}
