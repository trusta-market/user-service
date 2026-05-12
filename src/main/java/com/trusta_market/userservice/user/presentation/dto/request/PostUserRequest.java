package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

// 신규 유저 생성 요청 DTO
public record PostUserRequest(
    String name
) {
    public PostUserRequest {
        if (name == null || name.isBlank()) throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
    }
}
