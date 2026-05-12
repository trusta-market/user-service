package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;

// 유저 생성 명령 객체
public record CreateUserCommand(
    KeycloakId keycloakId,
    Email email,
    Name name
) {
    public CreateUserCommand {
        if (keycloakId == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (email == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (name == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
    }
}
