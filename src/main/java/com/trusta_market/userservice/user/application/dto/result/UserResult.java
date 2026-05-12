package com.trusta_market.userservice.user.application.dto.result;

import com.trusta_market.userservice.user.domain.entity.User;
import com.trusta_market.userservice.user.domain.vo.*;

// 유저 결과 데이터 객체
public record UserResult(
    UserId userId,
    Email email,
    Name name,
    Role role,
    UserStatus userStatus,
    Membership membership
) {
    // 엔티티로부터 Result 객체 생성
    public static UserResult from(User user) {
        return new UserResult(
            user.getUserId(),
            user.getEmail(),
            user.getName(),
            user.getRole(),
            user.getUserStatus(),
            user.getMembership()
        );
    }
}
