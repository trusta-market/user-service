package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;

// 유저 생성 커맨드 (null 검증 포함)
public record CreateUserCommand(KeycloakId keycloakId, Email email, Name name) {
    public CreateUserCommand {
        if (keycloakId == null) throw new UserException(UserErrorCode.INVALID_INPUT);
        if (email == null) throw new UserException(UserErrorCode.INVALID_INPUT);
        if (name == null) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
