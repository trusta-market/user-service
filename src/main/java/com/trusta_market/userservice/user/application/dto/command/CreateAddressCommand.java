package com.trusta_market.userservice.user.application.dto.command;

import java.util.UUID;

// 배송지 생성 명령 객체
public record CreateAddressCommand(
    UUID userId,
    String recipientName,
    String recipientPhone,
    String zipCode,
    String address,
    String addressDetail
) {
    public CreateAddressCommand {
        if (userId == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (recipientName == null || recipientName.isBlank()) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_NAME_FORMAT);
        if (recipientPhone == null || recipientPhone.isBlank()) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_PHONE_FORMAT);
        if (zipCode == null || zipCode.isBlank()) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_ZIP_CODE);
        if (address == null || address.isBlank()) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
    }
}
