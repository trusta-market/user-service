package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// UserErrorCode를 공통 ErrorCodeSpec으로 변환하는 어댑터
@Getter
public class UserErrorCodeAdapter implements ErrorCodeSpec {

    private final HttpStatus status;
    private final String code;
    private final String message;

    private UserErrorCodeAdapter(UserErrorCode errorCode) {
        this.status = HttpStatus.valueOf(errorCode.getStatus());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static UserErrorCodeAdapter of(UserErrorCode errorCode) {
        return new UserErrorCodeAdapter(errorCode);
    }

    @Override
    public String getField() {
        return null;
    }
}
