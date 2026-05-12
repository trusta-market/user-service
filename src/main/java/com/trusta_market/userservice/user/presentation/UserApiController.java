package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.user.application.dto.command.CreateUserCommand;
import com.trusta_market.userservice.user.application.dto.command.SignUpCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateUserCommand;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import com.trusta_market.userservice.user.infrastructure.security.SecurityUtil;
import com.trusta_market.userservice.user.presentation.dto.request.PatchUserRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserRequest;
import com.trusta_market.userservice.user.presentation.dto.request.SignUpRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 사용자 프로필 관리 외부 API 컨트롤러
@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserUseCase userUseCase;

    public UserApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    // 통합 회원가입 (Keycloak 계정 + 서비스 프로필 동시 생성)
    @PostMapping("/signup")
    public ResponseEntity<GetUserResponse> signup(@Valid @RequestBody SignUpRequest request) {
        var result = userUseCase.signUp(new SignUpCommand(
                Email.of(request.email()), request.password(), Name.of(request.name())));
        return ResponseEntity.status(201).body(GetUserResponse.from(result));
    }

    // 신규 유저 생성(회원가입) API
    @PostMapping
    public ResponseEntity<GetUserResponse> createUser(@RequestBody PostUserRequest request) {
        String keycloakId = SecurityUtil.getCurrentUserId().map(UUID::toString)
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));
        String email = SecurityUtil.getCurrentUserEmail()
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));
        
        var result = userUseCase.createUser(new CreateUserCommand(
                KeycloakId.of(keycloakId), Email.of(email), Name.of(request.name())));
        return ResponseEntity.status(201).body(GetUserResponse.from(result));
    }

    // 내 프로필 정보 조회 API
    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getMyUser() {
        String keycloakId = SecurityUtil.getCurrentUserId().map(UUID::toString)
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));
        var result = userUseCase.getUserByKeycloakId(KeycloakId.of(keycloakId));
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 내 프로필 정보 수정 API
    @PatchMapping("/me")
    public ResponseEntity<GetUserResponse> updateMyUser(@RequestBody PatchUserRequest request) {
        String keycloakId = SecurityUtil.getCurrentUserId().map(UUID::toString)
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));
        var result = userUseCase.updateUser(KeycloakId.of(keycloakId),
                new UpdateUserCommand(Name.of(request.name())));
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 회원 탈퇴 API (Soft Delete)
    @DeleteMapping("/me")
    public ResponseEntity<Void> withdrawMyUser() {
        String keycloakId = SecurityUtil.getCurrentUserId().map(UUID::toString)
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));
        userUseCase.withdrawUser(KeycloakId.of(keycloakId));
        return ResponseEntity.noContent().build();
    }
}
