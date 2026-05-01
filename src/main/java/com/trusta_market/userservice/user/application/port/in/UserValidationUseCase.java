package com.trusta_market.userservice.user.application.port.in;

import java.util.UUID;

public interface UserValidationUseCase {
    void validateActiveUser(UUID userId);
    void validateUserCanMutate(UUID userId);
}
