package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

public record PatchAddressRequest(
        String recipientName,
        String recipientPhone,
        String zipCode,
        String address,
        String addressDetail
) {
    public PatchAddressRequest {
        if (recipientName != null && recipientName.isBlank()) throw new UserException(UserErrorCode.INVALID_NAME_FORMAT);
        if (recipientPhone != null && recipientPhone.isBlank()) throw new UserException(UserErrorCode.INVALID_PHONE_FORMAT);
        if (zipCode != null && zipCode.isBlank()) throw new UserException(UserErrorCode.INVALID_ZIP_CODE);
        if (address != null && address.isBlank()) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
