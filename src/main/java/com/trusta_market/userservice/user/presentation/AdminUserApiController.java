package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.suspension.application.port.in.SuspensionUseCase;
import com.trusta_market.userservice.common.pagination.DomainPage;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.vo.UserStatus;
import com.trusta_market.userservice.user.presentation.dto.request.ChangeRoleRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserRejectRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostUserSuspendRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetUserResponse;
import com.trustamarket.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 관리자용 유저 관리 API 컨트롤러
@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin · User", description = "관리자 전용 유저 관리 API (ADMIN role 필요)")
public class AdminUserApiController {

    private final UserUseCase userUseCase;
    private final SuspensionUseCase suspensionUseCase;

    public AdminUserApiController(UserUseCase userUseCase,
                                  SuspensionUseCase suspensionUseCase) {
        this.userUseCase = userUseCase;
        this.suspensionUseCase = suspensionUseCase;
    }

    // 유저 목록 페이징 조회 API
    @Operation(summary = "유저 목록 페이징 조회", description = "status / role 필터로 유저 페이지 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<DomainPage<GetUserResponse>>> getUserList(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) Role role) {
        var result = userUseCase.getUserPage(page, size, userStatus, role).map(GetUserResponse::from);
        return ResponseEntity.ok(CommonResponse.of(200, result));
    }

    // 유저 상세 정보 조회 API (추가)
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<GetUserResponse>> getUserDetail(@PathVariable UUID userId) {
        var result = userUseCase.getUser(userId);
        return ResponseEntity.ok(CommonResponse.of(200, GetUserResponse.from(result)));
    }

    // 유저 활동 정지 API
    @PostMapping("/{userId}/suspend")
    public ResponseEntity<CommonResponse<GetUserResponse>> suspendUser(@PathVariable UUID userId, @RequestBody PostUserSuspendRequest request) {
        suspensionUseCase.suspendUser(userId, request.reason(), request.expiresAt());
        var result = userUseCase.getUser(userId);
        return ResponseEntity.ok(CommonResponse.of(200, GetUserResponse.from(result)));
    }

    // 유저 활동 정지 해제 API
    @PatchMapping("/{userId}/unsuspend")
    public ResponseEntity<CommonResponse<GetUserResponse>> unsuspendUser(@PathVariable UUID userId, @RequestBody PostUserRejectRequest request) {
        suspensionUseCase.unsuspendUser(userId, request.reason());
        var result = userUseCase.getUser(userId);
        return ResponseEntity.ok(CommonResponse.of(200, GetUserResponse.from(result)));
    }

    // 유저 역할 변경 API (MEMBER ↔ INSPECTOR)
    @Operation(summary = "유저 역할 변경", description = "MEMBER ↔ INSPECTOR. ADMIN 으로 승격은 가능, ADMIN 강등은 불가")
    @PatchMapping("/{userId}/role")
    public ResponseEntity<CommonResponse<GetUserResponse>> changeUserRole(@PathVariable UUID userId,
                                                          @RequestBody ChangeRoleRequest request) {
        var result = userUseCase.changeUserRole(userId, request.role());
        return ResponseEntity.ok(CommonResponse.of(200, GetUserResponse.from(result)));
    }
}
