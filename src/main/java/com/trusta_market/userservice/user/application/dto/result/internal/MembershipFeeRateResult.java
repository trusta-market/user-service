package com.trusta_market.userservice.user.application.dto.result.internal;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipFeeRateResult(
        UUID userId,
        Membership membership,
        double feeRate
) {
    public static MembershipFeeRateResult from(User user) {
        Membership membership = user.getMembership();
        return new MembershipFeeRateResult(
                user.getUserId().value(),
                membership,
                membership.getFeeRate()
        );
    }
}
