package com.cemware.sally.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF(Cross-Site Request Forgery) 보호 기능 비활성화
                /* - REST API / Postman 테스트 / 프론트 분리 환경에서는 CSRF 토큰이 필요하지 않음
                 * - 세션 기반 로그인(form POST) 서비스가 아니므로 disable 하는 것이 일반적
                 */
                .csrf(AbstractHttpConfigurer::disable)

                // 2. URL 별 권한 규칙
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // 관리자: ROLE_ADMIN 있어야 접근 가능
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 사용자: ROLE_USER 또는 ROLE_ADMIN 있어야 접근 가능
                        .anyRequest().permitAll()  // 나머지는 모두 허용
                )

                // 3. 기본 로그인 폼 사용
                .formLogin(Customizer.withDefaults()) // Spring Security 기본 로그인 페이지 자동 제공

                // 4. 기본 로그아웃 사용
                .logout(Customizer.withDefaults()); // 기본 로그아웃 기능 사용

        return http.build();
    }
}

