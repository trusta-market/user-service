package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.user.application.dto.command.CreateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateAddressCommand;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.presentation.dto.request.PatchAddressRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostAddressRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetAddressResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// 사용자 배송지 관리 외부 API 컨트롤러
@RestController
@RequestMapping("/api/v1/users/addresses")
public class AddressApiController {

    private final UserUseCase userUseCase;

    public AddressApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    // 유저의 배송지 목록 조회 API
    @GetMapping("/{userId}")
    public ResponseEntity<List<GetAddressResponse>> getAddressList(@PathVariable UUID userId) {
        return ResponseEntity.ok(userUseCase.getAddressList(userId).stream()
                .map(GetAddressResponse::from)
                .toList());
    }

    // 신규 배송지 등록 API
    @PostMapping("/{userId}")
    public ResponseEntity<GetAddressResponse> createAddress(@PathVariable UUID userId, @Valid @RequestBody PostAddressRequest request) {
        var result = userUseCase.createAddress(new CreateAddressCommand(
                userId, request.recipientName(), request.recipientPhone(),
                request.zipCode(), request.address(), request.addressDetail()));
        return ResponseEntity.status(201).body(GetAddressResponse.from(result));
    }

    // 기존 배송지 수정 API
    @PatchMapping("/{userId}/{addressId}")
    public ResponseEntity<GetAddressResponse> updateAddress(@PathVariable UUID userId, @PathVariable UUID addressId, @RequestBody PatchAddressRequest request) {
        var result = userUseCase.updateAddress(userId, addressId, new UpdateAddressCommand(
                request.recipientName(), request.recipientPhone(), request.zipCode(),
                request.address(), request.addressDetail()));
        return ResponseEntity.ok(GetAddressResponse.from(result));
    }

    // 배송지 삭제 API
    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID userId, @PathVariable UUID addressId) {
        userUseCase.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    // 대표 배송지 설정 변경 API
    @PatchMapping("/{userId}/{addressId}/default")
    public ResponseEntity<Void> changeDefault(@PathVariable UUID userId, @PathVariable UUID addressId) {
        userUseCase.changeAddressDefault(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
