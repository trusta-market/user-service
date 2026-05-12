package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

public record PostUserRejectRequest(String reason) {
    public PostUserRejectRequest {
        if (reason == null || reason.isBlank()) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
