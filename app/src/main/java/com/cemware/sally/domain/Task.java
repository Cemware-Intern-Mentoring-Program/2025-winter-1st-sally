package com.cemware.sally.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // GROUP 테이블의 id를 FK로 가지는 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 20)
    private String status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    protected Task() {}

    public Task(Group group, String title, String description,
                String status, LocalDate dueDate) {
        this.group = group;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getter/Setter
    public Long getId() { return id; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
