package com.trusta_market.userservice.user.presentation.exception;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trustamarket.common.exception.ErrorCodeSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// UserErrorCode를 공통 ErrorCodeSpec으로 변환하는 어댑터
@RequiredArgsConstructor
public class UserErrorCodeAdapter implements ErrorCodeSpec {

    private final UserErrorCode errorCode;

    public static UserErrorCodeAdapter of(UserErrorCode errorCode) {
        return new UserErrorCodeAdapter(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.valueOf(errorCode.getStatus());
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getField() {
        return null;
    }
}
