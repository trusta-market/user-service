package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.Password;

public record SignUpCommand(
    Email email,
    Password password,
    Name name
) {
    public SignUpCommand {
        if (email == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (password == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
        if (name == null) throw new com.trusta_market.userservice.user.domain.exception.UserException(com.trusta_market.userservice.user.domain.exception.UserErrorCode.INVALID_INPUT);
    }
}
