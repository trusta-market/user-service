package com.trusta_market.userservice.user.presentation.dto.response.internal;

import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipInternalResponse(
        UUID userId,
        Membership membership
) {
    // Result 객체를 Response DTO로 변환합니다.
    public static MembershipInternalResponse from(com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult result) {
        return new MembershipInternalResponse(result.userId(), result.membership());
    }
}
