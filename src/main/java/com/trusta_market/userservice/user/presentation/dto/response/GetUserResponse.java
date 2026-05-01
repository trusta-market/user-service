package com.trusta_market.userservice.user.presentation.dto.response;

import com.trusta_market.userservice.user.application.dto.result.UserResult;
import java.util.UUID;

public record GetUserResponse(
        UUID userId,
        String email,
        String name,
        String role,
        String status,
        String membership
) {
    public static GetUserResponse from(UserResult result) {
        return new GetUserResponse(
                result.userId(),
                result.email(),
                result.name(),
                result.role(),
                result.status(),
                result.membership()
        );
    }
}
