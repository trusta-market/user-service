package com.trusta_market.userservice.user.application.dto.command;

import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.Name;

public record SignUpCommand(
    Email email,
    String password,
    Name name
) {
}
