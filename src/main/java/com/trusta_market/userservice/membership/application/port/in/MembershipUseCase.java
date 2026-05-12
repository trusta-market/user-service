package com.trusta_market.userservice.membership.application.port.in;

import com.trusta_market.userservice.user.application.dto.result.internal.MembershipResult;

import java.util.UUID;

public interface MembershipUseCase {
    MembershipResult getMembership(UUID userId);
}
