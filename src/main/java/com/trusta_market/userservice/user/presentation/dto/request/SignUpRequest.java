package com.trusta_market.userservice.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

// 통합 회원가입 요청 DTO (이메일, 비밀번호, 이름 포함)

public record SignUpRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String name
) {
    public SignUpRequest {
        if (email == null || email.isBlank()) throw new UserException(UserErrorCode.INVALID_EMAIL_FORMAT);
        if (password == null || password.isBlank() || password.length() < 8) throw new UserException(UserErrorCode.INVALID_INPUT);
        if (name == null || name.isBlank()) throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
    }
}
