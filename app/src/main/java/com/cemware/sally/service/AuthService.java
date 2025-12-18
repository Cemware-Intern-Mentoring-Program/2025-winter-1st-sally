package com.cemware.sally.service;

import com.cemware.sally.domain.User;
import com.cemware.sally.dto.auth.LoginRequest;
import com.cemware.sally.dto.auth.SignupRequest;
import com.cemware.sally.repository.UserRepository;
import com.cemware.sally.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // 회원가입 로직
    public void signup(SignupRequest request) {

        // 1) username 중복 체크
        userRepository.findByUsername(request.username())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
                });

        // 2) 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 3) User 엔티티 생성 (기본 role = USER)
        User user = User.builder()
                .username(request.username())
                .password(encodedPassword)
                .role("USER")
                .build();

        // 4) 저장
        userRepository.save(user);
    }

    // 로그인 + JWT 발급
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // UserDetails 객체로 변환해서 JwtProvider에 넘김
        org.springframework.security.core.userdetails.UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())        // "USER"
                        .build();

        return jwtProvider.generateToken(userDetails);
    }
}
