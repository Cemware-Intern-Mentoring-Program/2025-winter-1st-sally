package com.cemware.sally.repository;

import com.cemware.sally.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // 특정 그룹에 속한 Task 목록 조회
    List<Task> findByGroupId(Long groupId);

    // 특정 그룹에 속한 Task 전부 삭제
    void deleteByGroupId(Long groupId);
}


