package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipResult(
        UUID userId,
        Membership membership
) {
    // 사용자 엔티티를 등급 조회 결과로 변환한다.
    public static MembershipResult from(com.trusta_market.userservice.user.domain.entity.User user) {
        return new MembershipResult(user.getUserId().value(), user.getMembership());
    }
}
