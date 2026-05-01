package com.trusta_market.userservice.user.application.dto.query;

import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;

import java.util.UUID;

public record GetUserQuery(
        UUID userId,
        String keycloakId,
        UserStatus userStatus,
        Role role
) {
}
