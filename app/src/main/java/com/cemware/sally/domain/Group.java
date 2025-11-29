package com.cemware.sally.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // PK

    // 다대일 관계 (여러 그룹이 한 유저를 가질 수 있음)
    // 기본 fetch = EAGER → 하지만 성능상 LAZY 사용이 일반적
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;       // 그룹 이름

    @Column(length = 255)
    private String description; // 그룹 설명

    // 생성 시 필요한 필드만 받는 생성자
    // 이 생성자를 통해서만 Group을 생성하도록 설계
    public Group(User user, String name, String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }

    // 아래는 엔티티의 "행동"
    // setter가 아니라 updateName, updateDescription 등의 update 메서드로 설계
    // 이유: 엔티티가 단순 DTO가 아니라 '상태를 변경하는 도메인 객체'이기 때문

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
