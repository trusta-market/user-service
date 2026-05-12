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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

// 유저 서비스 보안 설정 (인증/인가 및 보안 필터 체인 정의)
// 유저 서비스 보안 설정
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class UserServiceSecurityConfig {

    private final LoginFilter loginFilter;


    // 회원가입 API 등 보안 필터링이 불필요한 공용 경로 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/api/v1/users/signup");
    }

    // LoginFilter의 자동 등록 방지
    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilterRegistration(LoginFilter filter) {
        FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false); // Servlet 글로벌 필터 등록 방지
        return registration;
    }

    // HTTP 보안 설정(CSRF, 세션, 경로별 권한 등)을 정의하는 메인 필터 체인
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/**", "/internal/**", "/swagger-ui/**", "/v3/api-docs/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/signup").permitAll()
                        .requestMatchers("/internal/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        if (loginFilter != null) {
            http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }
}
