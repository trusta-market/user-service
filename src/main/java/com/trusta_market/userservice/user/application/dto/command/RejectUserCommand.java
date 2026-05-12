package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;

public record RejectUserCommand(String reason) {
    public RejectUserCommand {
        if (reason == null || reason.isBlank()) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
