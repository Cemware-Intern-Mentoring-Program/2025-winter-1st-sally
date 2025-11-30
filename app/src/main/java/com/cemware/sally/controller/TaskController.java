package com.cemware.sally.controller;

import com.cemware.sally.domain.Task;
import com.cemware.sally.domain.TaskStatus;
import com.cemware.sally.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //1. 할 일 생성하기 (POST /tasks)
    @PostMapping
    public CreateTaskResponse createTask(@RequestBody CreateTaskRequest request) {
        // String으로 온 status를 Enum으로 변환 (null 허용)
        TaskStatus status = request.getStatus() != null
                ? TaskStatus.valueOf(request.getStatus())
                : null;

        Long taskId = taskService.createTask(
                request.getGroupId(),
                request.getTitle(),
                request.getDescription(),
                status,
                request.getDueDate()
        );

        return new CreateTaskResponse(taskId);
    }

    //2. 할 일 수정하기 (PUT /tasks/{id})
    @PutMapping("/{id}")
    public void updateTask(@PathVariable("id") Long taskId,
                           @RequestBody UpdateTaskRequest request) {

        TaskStatus status = request.getStatus() != null
                ? TaskStatus.valueOf(request.getStatus())
                : null;

        taskService.updateTask(
                taskId,
                request.getTitle(),
                request.getDescription(),
                status,
                request.getDueDate()
        );
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


    /* ====================== 요청/응답 DTO 클래스들 ====================== */

    // 할 일 생성 요청: { "groupId": 1, "title": "...", "description": "...", "status": "TODO", "dueDate": "2025-12-31" }
    public static class CreateTaskRequest {
        private Long groupId;
        private String title;
        private String description;
        private String status;    // "TODO", "DOING", "DONE"
        private LocalDate dueDate;

        public Long getGroupId() {
            return groupId;
        }
        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public LocalDate getDueDate() {
            return dueDate;
        }
        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }
    }

    // 할 일 생성 응답: { "id": 1 }
    public static class CreateTaskResponse {
        private Long id;

        public CreateTaskResponse(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    // 할 일 수정 요청: 생성과 거의 동일
    public static class UpdateTaskRequest {
        private String title;
        private String description;
        private String status;
        private LocalDate dueDate;

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public LocalDate getDueDate() {
            return dueDate;
        }
        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }
    }

    // 할 일 조회 응답
    public static class TaskResponse {
        private Long id;
        private Long groupId;
        private String title;
        private String description;
        private String status;
        private LocalDate dueDate;

        public TaskResponse(Long id,
                            Long groupId,
                            String title,
                            String description,
                            String status,
                            LocalDate dueDate) {
            this.id = id;
            this.groupId = groupId;
            this.title = title;
            this.description = description;
            this.status = status;
            this.dueDate = dueDate;
        }

        public Long getId() {
            return id;
        }
        public Long getGroupId() {
            return groupId;
        }
        public String getTitle() {
            return title;
        }
        public String getDescription() {
            return description;
        }
        public String getStatus() {
            return status;
        }
        public LocalDate getDueDate() {
            return dueDate;
        }
    }

    //컨트롤러 연결 확인용
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }
}
