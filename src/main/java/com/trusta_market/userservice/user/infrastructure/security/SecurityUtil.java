package com.trusta_market.userservice.user.infrastructure.security;

import com.trustamarket.common.config.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * SecurityContextHolder에서 현재 인증된 유저 정보를 추출하는 유틸리티 클래스입니다.
 * Common 라이브러리의 LoginFilter가 주입한 UserDetailsImpl 정보를 기반으로 작동합니다.
 */
public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * 현재 인증된 유저의 UUID(Keycloak ID 기반)를 반환합니다.
     */
    public static Optional<UUID> getCurrentUserId() {
        return getPrincipal().map(UserDetailsImpl::getUuid);
    }

    /**
     * 현재 인증된 유저의 이메일을 반환합니다.
     */
    public static Optional<String> getCurrentUserEmail() {
        return getPrincipal().map(UserDetailsImpl::getEmail);
    }

    private static Optional<UserDetailsImpl> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl principal) {
            return Optional.of(principal);
        }
        return Optional.empty();
    }
}
