package com.cemware.sally.controller;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.Task;
import com.cemware.sally.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    //1. 그룹 생성하기 (POST /groups)
    //- 어떤 유저(userId)가 name, description으로 새 그룹을 만든다.
    @PostMapping
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest request) {
        Long groupId = groupService.createGroup(
                request.getUserId(),
                request.getName(),
                request.getDescription()
        );
        return new CreateGroupResponse(groupId);
    }

    //2. 그룹 이름 수정하기 (PUT /groups/{id})
    @PutMapping("/{id}")
    public void updateGroupName(@PathVariable("id") Long groupId,
                                @RequestBody UpdateGroupNameRequest request) {
        groupService.updateGroupName(groupId, request.getName());
    }

    //3. 그룹 삭제하기 (DELETE /groups/{id}, 하위 할 일 삭제)
    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    //4. 그룹 불러오기 (GET /groups/{id})
    //- 그룹 정보 + 하위 할 일(Task) 목록을 함께 반환
    @GetMapping("/{id}")
    public GroupDetailResponse getGroup(@PathVariable("id") Long groupId) {
        GroupService.GroupWithTasks result = groupService.getGroupWithTasks(groupId);

        Group group = result.getGroup();
        List<Task> tasks = result.getTasks();

        // Task 엔티티 → 응답용 DTO로 변환
        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus() != null ? task.getStatus().name() : null,
                        task.getDueDate()
                ))
                .collect(Collectors.toList());

        return new GroupDetailResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                taskResponses
        );
    }

    /* ====================== 요청/응답 DTO 클래스들 ====================== */

    // 그룹 생성 요청 바디: { "userId": 1, "name": "공부", "description": "설명" }
    public static class CreateGroupRequest {
        private Long userId;
        private String name;
        private String description;

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
    }

    // 그룹 생성 응답: { "id": 1 }
    public static class CreateGroupResponse {
        private Long id;

        public CreateGroupResponse(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    // 그룹 이름 수정 요청 바디: { "name": "새 이름" }
    public static class UpdateGroupNameRequest {
        private String name;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    // 그룹 상세 응답: 그룹 정보 + 하위 Task 목록
    public static class GroupDetailResponse {
        private Long id;
        private String name;
        private String description;
        private List<TaskResponse> tasks;

        public GroupDetailResponse(Long id,
                                   String name,
                                   String description,
                                   List<TaskResponse> tasks) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tasks = tasks;
        }

        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
        public List<TaskResponse> getTasks() {
            return tasks;
        }
    }

    // 하위 Task 하나를 표현하는 DTO
    public static class TaskResponse {
        private Long id;
        private String title;
        private String description;
        private String status;      // "TODO", "DOING", "DONE"
        private LocalDate dueDate;  // 마감일

        public TaskResponse(Long id,
                            String title,
                            String description,
                            String status,
                            LocalDate dueDate) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.status = status;
            this.dueDate = dueDate;
        }

        public Long getId() {
            return id;
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
