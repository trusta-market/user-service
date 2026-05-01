package com.trusta_market.userservice.user.presentation;

import com.trusta_market.userservice.user.application.dto.command.CreateAddressCommand;
import com.trusta_market.userservice.user.application.dto.command.UpdateAddressCommand;
import com.trusta_market.userservice.user.application.port.in.UserUseCase;
import com.trusta_market.userservice.user.presentation.dto.request.PatchAddressRequest;
import com.trusta_market.userservice.user.presentation.dto.request.PostAddressRequest;
import com.trusta_market.userservice.user.presentation.dto.response.GetAddressResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/addresses")
public class AddressApiController {

    private final UserUseCase userUseCase;

    public AddressApiController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<GetAddressResponse>> getAddressList(@PathVariable UUID userId) {
        return ResponseEntity.ok(userUseCase.getAddressList(userId).stream()
                .map(GetAddressResponse::from)
                .toList());
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GetAddressResponse> createAddress(@PathVariable UUID userId, @Valid @RequestBody PostAddressRequest request) {
        var result = userUseCase.createAddress(new CreateAddressCommand(
                userId, 
                request.recipientName(), 
                request.recipientPhone(), 
                request.zipCode(), 
                request.address(), 
                request.addressDetail()
        ));
        return ResponseEntity.status(201).body(GetAddressResponse.from(result));
    }

    @PatchMapping("/{userId}/{addressId}")
    public ResponseEntity<GetAddressResponse> updateAddress(@PathVariable UUID userId, @PathVariable UUID addressId, @RequestBody PatchAddressRequest request) {
        var result = userUseCase.updateAddress(userId, addressId, new UpdateAddressCommand(
                request.recipientName(), 
                request.recipientPhone(), 
                request.zipCode(), 
                request.address(), 
                request.addressDetail()
        ));
        return ResponseEntity.ok(GetAddressResponse.from(result));
    }

    @DeleteMapping("/{userId}/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID userId, @PathVariable UUID addressId) {
        userUseCase.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/{addressId}/default")
    public ResponseEntity<Void> changeDefault(@PathVariable UUID userId, @PathVariable UUID addressId) {
        userUseCase.changeAddressDefault(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
