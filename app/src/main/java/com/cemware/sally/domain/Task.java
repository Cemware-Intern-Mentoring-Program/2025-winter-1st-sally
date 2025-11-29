package com.cemware.sally.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = 200)
    private String title;          // Task 제목

    @Column(columnDefinition = "text")
    private String description;    // 내용 (길이 제한 없음)

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskStatus status;     // 상태 (TODO, DOING, DONE)

    @Column(name = "due_date")
    private LocalDate dueDate;     // 마감일

    // 도메인 생성자 + Builder
    @Builder
    public Task(Group group,
                String title,
                String description,
                TaskStatus status,
                LocalDate dueDate) {

        this.group = group;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // 상태 변경 메서드들
    public void updateGroup(Group group) {
        this.group = group;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public void updateDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
