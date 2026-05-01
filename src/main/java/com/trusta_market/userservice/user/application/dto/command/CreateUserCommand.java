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
}
