package com.trusta_market.userservice.user.application.dto.result;

import com.trusta_market.userservice.user.domain.entity.User;
import java.util.UUID;

// 유저 결과 데이터 객체
public record UserResult(
    UUID userId,
    String email,
    String name,
    String role,
    String userStatus,
    String membership
) {
    // 엔티티로부터 Result 객체 생성
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
