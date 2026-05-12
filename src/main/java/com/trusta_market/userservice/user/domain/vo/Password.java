package com.trusta_market.userservice.user.domain.vo;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

// 유저 비밀번호 VO (생성 단계에서의 검증 담당)
public record Password(String value) {
    public Password {
        if (value == null || value.isBlank() || value.length() < 8) {
            throw new UserException(UserErrorCode.INVALID_INPUT);
        }
    }

    public static Password of(String value) {
        return new Password(value);
    }
}
