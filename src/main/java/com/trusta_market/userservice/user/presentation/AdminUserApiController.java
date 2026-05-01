package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserRejectRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserSuspendRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 관리자용 유저 관리 API 컨트롤러
@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserApiController {

    private final UserUseCase userUseCase;

    public AdminUserApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    // 유저 목록 페이징 조회 API
    @GetMapping
    public ResponseEntity<DomainPage<GetUserResponse>> getUserList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) Role role) {
        var results = userUseCase.getUserPage(page, size, userStatus, role);
        return ResponseEntity.ok(results.map(GetUserResponse::from));
    }

    // 유저 상세 정보 조회 API (추가)
    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUserDetail(@PathVariable UUID userId) {
        var result = userUseCase.getUser(userId);
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 유저 가입 승인 API
    @PatchMapping("/{userId}/approve")
    public ResponseEntity<GetUserResponse> approveUser(@PathVariable UUID userId) {
        var result = userUseCase.approveUser(userId);
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 유저 가입 거절 API
    @PostMapping("/{userId}/reject")
    public ResponseEntity<GetUserResponse> rejectUser(@PathVariable UUID userId, @RequestBody PostUserRejectRequest request) {
        var result = userUseCase.rejectUser(userId, request.reason());
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 유저 활동 정지 API
    @PostMapping("/{userId}/suspend")
    public ResponseEntity<GetUserResponse> suspendUser(@PathVariable UUID userId, @RequestBody PostUserSuspendRequest request) {
        var result = userUseCase.suspendUser(userId, request.reason(), request.expiresAt());
        return ResponseEntity.ok(GetUserResponse.from(result));
    }

    // 유저 활동 정지 해제 API
    @PatchMapping("/{userId}/unsuspend")
    public ResponseEntity<GetUserResponse> unsuspendUser(@PathVariable UUID userId, @RequestBody PostUserRejectRequest request) {
        var result = userUseCase.unsuspendUser(userId, request.reason());
        return ResponseEntity.ok(GetUserResponse.from(result));
    }
}
