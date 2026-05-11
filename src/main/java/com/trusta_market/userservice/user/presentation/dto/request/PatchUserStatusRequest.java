package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.vo.UserStatus;

public record PatchUserStatusRequest(UserStatus status) {
}
