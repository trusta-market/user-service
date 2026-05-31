package com.trusta_market.userservice.user.presentation.dto.response.internal;

import com.trusta_market.userservice.user.application.dto.result.internal.MembershipFeeRateResult;
import com.trusta_market.userservice.user.domain.vo.Membership;

import java.util.UUID;

public record MembershipFeeRateResponse(
        UUID userId,
        Membership membership,
        double feeRate
) {
    public static MembershipFeeRateResponse from(MembershipFeeRateResult result) {
        return new MembershipFeeRateResponse(
                result.userId(),
                result.membership(),
                result.feeRate()
        );
    }
}
