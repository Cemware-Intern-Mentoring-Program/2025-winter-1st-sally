package com.cemware.sally.domain;

import jakarta.persistence.*;

@Entity                     // 이 클래스가 JPA 엔티티임을 선언
@Table(name = "users")      // 실제 DB 테이블명 (USER는 예약어라 users 추천)
public class User {

    @Id                     // PK(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // MySQL의 AUTO_INCREMENT를 사용해 id 자동 증가
    private Long id;

    @Column(nullable = false, length = 50)
    // nullable=false → username은 반드시 값이 있어야 함(Not Null)
    // length=50 → varchar(50) 설정
    private String username;

    // 기본 생성자 (JPA가 엔티티를 만들 때 반드시 필요)
    protected User() {}

    // 편의를 위한 생성자
    public User(String username) {
        this.username = username;
    }

    // Getter/Setter (Lombok 안 쓴 버전)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
