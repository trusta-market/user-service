package com.trusta_market.userservice.user.presentation.dto.request;

import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.domain.vo.Role;

public record ChangeRoleRequest(Role role) {
    public ChangeRoleRequest {
        if (role == null) throw new UserException(UserErrorCode.INVALID_INPUT);
    }
}
