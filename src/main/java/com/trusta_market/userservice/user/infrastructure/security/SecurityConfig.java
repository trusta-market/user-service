package com.trusta_market.userservice.user.infrastructure.security;

import com.trustamarket.common.config.security.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 보안 설정 클래스
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 메서드 단위 보안 활성화 (@PreAuthorize 사용 가능)
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginFilter loginFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 정책: Stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/internal/**").permitAll() // 내부 호출 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // 문서 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // URL 기반 권한 제한
                        .anyRequest().authenticated()) // 나머지 인증 필요
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class) // 로그인 필터 등록
                .build();
    }
}
