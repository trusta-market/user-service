package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserRejectRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserSuspendRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class AdminUserApiController {

    private final UserUseCase userUseCase;

    public AdminUserApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    // 유저 목록 조회 (관리자용)
    @GetMapping
    public ResponseEntity<DomainPage<GetUserResponse>> getUserList(Integer page, Integer size, UserStatus userStatus, Role role) {
        int resolvedPage = page == null ? 0 : page;
        int resolvedSize = size == null ? 20 : size;
        var resultPage = userUseCase.getUserPage(resolvedPage, resolvedSize, userStatus, role);
        return ResponseEntity.ok(resultPage.map(GetUserResponse::from));
    }

    // 유저 상세 조회 (관리자용)
    @GetMapping("/{userId}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(GetUserResponse.from(userUseCase.getUser(userId)));
    }

    // 유저 가입 승인
    @PostMapping("/{userId}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable UUID userId) {
        userUseCase.approveUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 유저 가입 거절
    @PostMapping("/{userId}/reject")
    public ResponseEntity<Void> rejectUser(@PathVariable UUID userId, @RequestBody PostUserRejectRequest request) {
        userUseCase.rejectUser(userId, request.reason());
        return ResponseEntity.noContent().build();
    }

    // 유저 계정 정지
    @PostMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable UUID userId, @RequestBody PostUserSuspendRequest request) {
        userUseCase.suspendUser(userId, request.reason(), request.expiresAt());
        return ResponseEntity.noContent().build();
    }

    // 유저 계정 정지 해제
    @PostMapping("/{userId}/unsuspend")
    public ResponseEntity<Void> unsuspendUser(@PathVariable UUID userId, @RequestBody PostUserRejectRequest request) {
        userUseCase.unsuspendUser(userId, request.reason());
        return ResponseEntity.noContent().build();
    }
}
