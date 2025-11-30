package com.cemware.sally.controller;

import com.cemware.sally.domain.Task;
import com.cemware.sally.domain.TaskStatus;
import com.cemware.sally.dto.task.CreateTaskRequest;
import com.cemware.sally.dto.task.CreateTaskResponse;
import com.cemware.sally.dto.task.TaskResponse;
import com.cemware.sally.dto.task.UpdateTaskRequest;
import com.cemware.sally.dto.task.TaskUpdateDto;
import com.cemware.sally.service.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //1. 할 일 생성하기 (POST /tasks)
    @PostMapping
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest request) {
        // String으로 온 status를 Enum으로 변환 (null 허용)
        TaskStatus status = request.status() != null
                ? TaskStatus.valueOf(request.status())
                : null;

        Long taskId = taskService.createTask(
                request.groupId(),
                request.title(),
                request.description(),
                status,
                request.dueDate()
        );

        return new CreateTaskResponse(taskId);
    }

    //2. 할 일 수정하기 (PUT /tasks/{id})
    @PutMapping("/{id}")
    public void updateTask(@PathVariable("id") Long taskId,
                           @RequestBody UpdateTaskRequest request) {

        TaskStatus status = request.status() != null
                ? TaskStatus.valueOf(request.status())
                : null;

        TaskUpdateDto dto = new TaskUpdateDto(
                request.title(),
                request.description(),
                status,
                request.dueDate()
        );

        taskService.updateTask(taskId, dto);

    }

    //3. 할 일 삭제하기 (DELETE /tasks/{id})
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") Long taskId) {
        taskService.deleteTask(taskId);
    }

    //4. 할 일 읽기 (GET /tasks/{id})
    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable("id") Long taskId) {
        Task task = taskService.getTask(taskId);

        String status = (task.getStatus() != null)
                ? task.getStatus().name()
                : null;

        return new TaskResponse(
                task.getId(),
                task.getGroup().getId(),
                task.getTitle(),
                task.getDescription(),
                status,
                task.getDueDate()
        );
    }

}
