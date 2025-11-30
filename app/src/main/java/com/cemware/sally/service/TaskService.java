package com.cemware.sally.service;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.Task;
import com.cemware.sally.domain.TaskStatus;
import com.cemware.sally.dto.task.TaskUpdateDto;
import com.cemware.sally.repository.GroupRepository;
import com.cemware.sally.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;

    //1. 할 일 생성하기
    public Long createTask(Long groupId,
                           String title,
                           String description,
                           TaskStatus status,
                           LocalDate dueDate) {

        // 1) 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        // 2) Task 엔티티 생성
        Task task = Task.builder()
                .group(group)
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .build();

        // 3) 저장 후 반환된 전체 엔티티에서 PK(ID)만 꺼내서 반환
        Task saved = taskRepository.save(task);
        return saved.getId();
    }

    //2. 할 일 수정하기
    public void updateTask(Long taskId, TaskUpdateDto dto) {

        // 1) Task 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("할 일이 존재하지 않습니다. id=" + taskId));

        // 2) 상태 변경
        task.updateByDto(dto);

    }

    //3. 할 일 삭제하기
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("할 일이 존재하지 않습니다. id=" + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    //4. 할 일 읽기
    @Transactional
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("할 일이 존재하지 않습니다. id=" + taskId));
    }
}
