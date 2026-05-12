package com.trusta_market.userservice.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;

// 신규 배송지 등록 요청 DTO (입력값 검증 포함)

public record PostAddressRequest(
        @NotBlank String recipientName,
        @NotBlank String recipientPhone,
        @NotBlank String zipCode,
        @NotBlank String address,
        String addressDetail
) {
    public PostAddressRequest {
        if (recipientName == null || recipientName.isBlank()) throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
        if (recipientPhone == null || recipientPhone.isBlank()) throw new UserException(UserErrorCode.INVALID_PHONE_FORMAT);
        if (zipCode == null || zipCode.isBlank()) throw new UserException(UserErrorCode.INVALID_ZIP_CODE);
        if (address == null || address.isBlank()) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
