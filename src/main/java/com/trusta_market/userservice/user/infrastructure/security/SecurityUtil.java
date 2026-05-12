package com.trusta_market.userservice.user.infrastructure.security;

import com.trustamarket.common.config.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

// SecurityContextHolder에서 현재 인증된 유저 정보를 추출하는 유틸리티
public class SecurityUtil {

    private SecurityUtil() {
    }

    // 현재 인증된 유저의 UUID(Keycloak ID 기반) 반환
    public static Optional<UUID> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();

        Object principal = authentication.getPrincipal();
        
        // 1. JWT 토큰 방식 (OAuth2 Resource Server)
        if (principal instanceof Jwt jwt) {
            try {
                return Optional.of(UUID.fromString(jwt.getSubject()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        
        // 2. UserDetailsImpl 방식 (기존 필터 방식)
        if (principal instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getUuid());
        }
        
        return Optional.empty();
    }

    // 현재 인증된 유저의 이메일 반환
    public static Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();

        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return Optional.ofNullable(jwt.getClaimAsString("email"));
        }

        if (principal instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails.getEmail());
        }

        return Optional.empty();
    }

    private static Optional<UserDetailsImpl> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }
}
