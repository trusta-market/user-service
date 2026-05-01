package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.user.application.dto.command.CreateUserCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateUserCommand;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import com.trusta_market.userservice.user.presentation.dto.request.PatchUserRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
public class UserApiController {

    private final UserUseCase userUseCase;

    public UserApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping("/users")
    public ResponseEntity<GetUserResponse> createUser(@RequestBody PostUserRequest request) {
        String keycloakId = SecurityUtil.getCurrentUserId()
                .map(UUID::toString)
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));
        String email = SecurityUtil.getCurrentUserEmail()
                .orElseThrow(() -> new RuntimeException("이메일 정보가 없습니다."));
        
        var result = userUseCase.createUser(new CreateUserCommand(
                KeycloakId.of(keycloakId),
                Email.of(email),
                Name.of(request.name())
        ));
        return ResponseEntity.status(201).body(GetUserResponse.from(result));
    }

    @GetMapping("/users/me")
    public ResponseEntity<GetUserResponse> getMyUser() {
        String keycloakId = SecurityUtil.getCurrentUserId()
                .map(UUID::toString)
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));
        var result = userUseCase.getUserByKeycloakId(KeycloakId.of(keycloakId));
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    @PatchMapping("/users/me")
    public ResponseEntity<GetUserResponse> updateMyUser(@RequestBody PatchUserRequest request) {
        String keycloakId = SecurityUtil.getCurrentUserId()
                .map(UUID::toString)
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));
        var result = userUseCase.updateUser(KeycloakId.of(keycloakId),
                new UpdateUserCommand(Name.of(request.name())));
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<Void> withdrawMyUser() {
        String keycloakId = SecurityUtil.getCurrentUserId()
                .map(UUID::toString)
                .orElseThrow(() -> new RuntimeException("인증 정보가 없습니다."));
        userUseCase.withdrawUser(KeycloakId.of(keycloakId));
        return ResponseEntity.noContent().build();
    }
}
