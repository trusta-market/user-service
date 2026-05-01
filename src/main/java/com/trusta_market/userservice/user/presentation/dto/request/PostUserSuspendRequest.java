package com.trusta_market.userservice.user.presentation.dto.request;

import java.time.LocalDateTime;

public record PostUserSuspendRequest(String reason, LocalDateTime expiresAt) {
}
