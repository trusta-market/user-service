package com.trusta_market.userservice.user.application.dto.command;

// 배송지 수정 명령 객체
import com.trusta_market.userservice.user.domain.vo.AddressDetail;
import com.trusta_market.userservice.user.domain.vo.AddressInfo;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.PhoneNumber;
import com.trusta_market.userservice.user.domain.vo.ZipCode;

// 배송지 수정 명령 객체
public record UpdateAddressCommand(
    Name recipientName,
    PhoneNumber recipientPhone,
    ZipCode zipCode,
    AddressInfo address,
    AddressDetail addressDetail
) {
    public UpdateAddressCommand {
        if (recipientName == null && recipientPhone == null && zipCode == null && address == null && addressDetail == null) {
            throw new com.trusta_market.userservice.user.domain.exception.UserException(
                com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        }
    }
}
