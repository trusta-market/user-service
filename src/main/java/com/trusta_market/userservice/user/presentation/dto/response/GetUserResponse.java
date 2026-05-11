package com.trusta_market.userservice.user.presentation.dto.response;

import com.trusta_market.userservice.user.application.dto.result.UserResult;
import java.util.UUID;

// 유저 정보 응답 객체
public record GetUserResponse(
    UUID userId,
    String email,
    String name,
    String role,
    String userStatus,
    String membership
) {
    // Result 객체로부터 Response 생성
    public static GetUserResponse from(UserResult result) {
        return new GetUserResponse(
            result.userId(),
            result.email(),
            result.name(),
            result.role(),
            result.userStatus(),
            result.membership()
        );
    }
}
