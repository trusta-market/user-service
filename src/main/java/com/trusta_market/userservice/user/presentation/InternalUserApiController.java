package com.trusta_market.userservice.user.presentation;


import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.presentation.dto.response.internal.MembershipInternalResponse;
import com.trusta_market.userservice.user.presentation.dto.response.internal.UserInternalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
public class InternalUserApiController {

    private final UserUseCase userUseCase;

    public InternalUserApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInternalResponse> getUser(@PathVariable UUID userId) {
        var result = userUseCase.getInternalUser(userId);
        return ResponseEntity.ok(UserInternalResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<List<UserInternalResponse>> getUserList(@RequestParam String userIds) {
        List<UUID> ids = Arrays.stream(userIds.split(",")).map(UUID::fromString).toList();
        var result = userUseCase.getInternalUserList(ids);
        return ResponseEntity.ok(result.stream().map(UserInternalResponse::from).toList());
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Void> validateUser(@PathVariable UUID userId) {
        userUseCase.validateInternalUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/membership")
    public ResponseEntity<MembershipInternalResponse> getMembership(@PathVariable UUID userId) {
        var result = userUseCase.getMembership(userId);
        return ResponseEntity.ok(MembershipInternalResponse.from(result));
    }
}
