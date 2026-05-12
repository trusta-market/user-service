package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.AddressDetail;
import com.trusta_market.userservice.user.domain.vo.AddressInfo;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.PhoneNumber;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.domain.vo.ZipCode;

// 배송지 생성 명령 객체
public record CreateAddressCommand(
    UserId userId,
    Name recipientName,
    PhoneNumber recipientPhone,
    ZipCode zipCode,
    AddressInfo address,
    AddressDetail addressDetail
) {
    public CreateAddressCommand {
        if (userId == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (recipientName == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_NAME_FORMAT);
        if (recipientPhone == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_PHONE_FORMAT);
        if (zipCode == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_ZIP_CODE);
        if (address == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
    }
}
