package com.cemware.sally.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "todo_group")   // MySQL 예약어 방지 → groups 사용 권장
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER 테이블의 id를 가져오는 FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    // user_id FK 컬럼으로 매핑됨
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    protected Group() {}

    public Group(User user, String name, String description) {
        this.user = user;
        this.name = name;
        this.description = description;
    }

    // Getter/Setter
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
