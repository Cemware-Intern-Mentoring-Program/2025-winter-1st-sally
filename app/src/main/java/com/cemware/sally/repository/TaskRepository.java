package com.cemware.sally.repository;

import com.cemware.sally.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// Task 엔티티용 Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 예시) 특정 그룹의 Task만 찾고 싶을 때
    // List<Task> findByGroupId(Long groupId);
}

