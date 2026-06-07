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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class UserServiceSecurityConfig {

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private LoginFilter loginFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/api/v1/users/signup");
    }

    @Bean
    @org.springframework.boot.autoconfigure.condition.ConditionalOnBean(LoginFilter.class)
    public FilterRegistrationBean<LoginFilter> loginFilterRegistration(LoginFilter filter) {
        FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false); // Servlet 글로벌 필터 등록 방지! (Spring Security 필터 체인 안에서만 돌게 함)
        return registration;
    }

    // /internal/** — 서비스 간 Feign 호출용. JWT 검증 없이 허용 (Gateway가 외부 차단).
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain internalSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/internal/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/**", "/swagger-ui/**", "/v3/api-docs/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/signup").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(org.springframework.security.config.Customizer.withDefaults()));

        if (loginFilter != null) {
            http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }
}
