package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

// 유저 프로필(이름) 수정 요청 DTO
public record PatchUserRequest(
    String name
) {
    public PatchUserRequest {
        if (name == null || name.isBlank()) throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
    }
}
