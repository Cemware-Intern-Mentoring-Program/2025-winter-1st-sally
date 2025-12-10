package com.cemware.sally.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity                     // JPA가 관리하는 엔티티임을 선언
@Table(name = "users")      // 매핑될 테이블 이름
@Getter                     // 모든 필드에 대한 getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA가 프록시 생성을 위해 기본 생성자를 필요로 함.
// 외부에서 new User() 못하게 PROTECTED로 막음.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // PK (자동 증가)

    @Column(nullable = false, unique = true, length = 50)
    private String username;            // 로그인 ID

    @Column(nullable = false)
    private String password;            // 암호화된 비밀번호

    @Column(nullable = false, length = 20)
    private String role;                // 예: "USER"

    // Builder + 도메인 생성자
    @Builder
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;       // 암호화된 비번으로 저장할 예정
        this.role = role;
    }

    // setter 대신 update 메서드 제공
    // "username이라는 상태를 변경한다"는 의미를 명확히 표현
    public void updateUsername(String username) {
        this.username = username;
    }
}