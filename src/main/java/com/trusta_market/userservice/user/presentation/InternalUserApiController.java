package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.membership.application.port.in.MembershipUseCase;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.presentation.dto.response.internal.MembershipInternalResponse;
import com.trusta_market.userservice.user.presentation.dto.response.internal.UserInternalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// 서비스 간 내부 통신용 유저 API 컨트롤러
@RestController
@RequestMapping("/internal/users")
public class InternalUserApiController {

    private final UserUseCase userUseCase;
    private final MembershipUseCase membershipUseCase;

    public InternalUserApiController(UserUseCase userUseCase,
                                     MembershipUseCase membershipUseCase) {
        this.userUseCase = userUseCase;
        this.membershipUseCase = membershipUseCase;
    }

    // 단건 유저 내부 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserInternalResponse> getUser(@PathVariable UUID userId) {
        var result = userUseCase.getInternalUser(userId);
        return ResponseEntity.ok(UserInternalResponse.from(result));
    }

    // 다건 유저 내부 정보 목록 조회
    @GetMapping
    public ResponseEntity<List<UserInternalResponse>> getUserList(@RequestParam String userIds) {
        List<UUID> ids = Arrays.stream(userIds.split(",")).map(UUID::fromString).toList();
        var result = userUseCase.getInternalUserList(ids);
        return ResponseEntity.ok(result.stream().map(UserInternalResponse::from).toList());
    }

    // 유저 멤버십 정보 조회
    @GetMapping("/{userId}/membership")
    public ResponseEntity<MembershipInternalResponse> getMembership(@PathVariable UUID userId) {
        var result = membershipUseCase.getMembership(userId);
        return ResponseEntity.ok(MembershipInternalResponse.from(result));
    }
}
