package com.trusta_market.userservice.user.application.dto.result;

import com.trusta_market.userservice.user.domain.entity.User;
import java.util.UUID;

public record UserResult(
        UUID userId,
        String email,
        String name,
        String role,
        String status,
        String membership
) {
    public static UserResult from(User user) {
        return new UserResult(
                user.getUserId().value(),
                user.getEmail().value(),
                user.getName().value(),
                user.getRole().name(),
                user.getUserStatus().name(),
                user.getMembership().name()
        );
    }
}
