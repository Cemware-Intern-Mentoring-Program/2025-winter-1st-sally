package com.cemware.sally.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    // Task 여러개는 하나의 Group에 소속됨 → ManyToOne
    // 마찬가지로 LAZY 사용해 성능 최적화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = 200)
    private String title;          // Task 제목

    @Column(columnDefinition = "text")
    private String description;    // 내용 (길이 제한 없음)

    @Column(length = 20)
    private String status;         // 상태 (예: TODO, DOING, DONE)

    @Column(name = "due_date")
    private LocalDate dueDate;     // 마감일

    // 도메인 생성자
    // Task는 생성 시 어떤 값들이 반드시 필요한지 정의
    public Task(Group group,
                String title,
                String description,
                String status,
                LocalDate dueDate) {

        this.group = group;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // 엔티티의 "상태 변경" 메서드
    // setter 대신 update 메서드 사용
    public void updateGroup(Group group) {
        this.group = group;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

