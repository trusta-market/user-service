package com.trusta_market.userservice.user.infrastructure.persistence.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof com.trustamarket.common.config.security.UserDetailsImpl userDetails) {
                return Optional.of(userDetails.getUuid());
            }

            try {
                return Optional.of(UUID.fromString(authentication.getName()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        };
    }
}
