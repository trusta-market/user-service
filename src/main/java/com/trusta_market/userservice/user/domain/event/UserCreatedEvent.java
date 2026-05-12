package com.trusta_market.userservice.user.domain.event;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.UserId;

import java.util.Objects;

/**
 * 신규 유저 생성 시 발생하는 도메인 이벤트
 * 지갑 생성, 메일 발송 등 후속 처리를 위해 사용됩니다.
 */
public record UserCreatedEvent(
        UserId userId,
        Email email,
        Name name
) {
    public UserCreatedEvent {
        if (userId == null || email == null || name == null) {
            throw new com.trusta_market.userservice.user.domain.exception.UserException(
                com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        }
    }

    public static UserCreatedEvent of(UserId userId, Email email, Name name) {
        return new UserCreatedEvent(userId, email, name);
    }
}
