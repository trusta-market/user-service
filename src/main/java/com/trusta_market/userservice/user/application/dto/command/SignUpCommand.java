package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;

public record SignUpCommand(
    Email email,
    String password,
    Name name
) {
    public SignUpCommand {
        if (email == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (password == null || password.isBlank()) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (name == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
    }
}
