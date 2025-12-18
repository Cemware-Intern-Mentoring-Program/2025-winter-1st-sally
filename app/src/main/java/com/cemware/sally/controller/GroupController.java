package com.cemware.sally.controller;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.Task;
import com.cemware.sally.dto.group.CreateGroupRequest;
import com.cemware.sally.dto.group.CreateGroupResponse;
import com.cemware.sally.dto.group.GroupDetailResponse;
import com.cemware.sally.dto.group.GroupWithTasksDto;
import com.cemware.sally.dto.group.UpdateGroupNameRequest;
import com.cemware.sally.dto.task.TaskResponse;
import com.cemware.sally.service.GroupService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    //1. 그룹 생성하기 (POST /groups)
    //- 어떤 유저(userId)가 name, description으로 새 그룹을 만든다.
    @PostMapping
    public CreateGroupResponse createGroup(
            @RequestBody CreateGroupRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        Long groupId = groupService.createGroupByUsername(
                user.getUsername(),
                request.name(),
                request.description()
        );
        return new CreateGroupResponse(groupId);
    }

    //2. 그룹 이름 수정하기 (PUT /groups/{id})
    @PutMapping("/{id}")
    public void updateGroupName(
            @PathVariable("id") Long groupId,
            @RequestBody UpdateGroupNameRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        groupService.updateGroupNameForUser(groupId, request.name(), user.getUsername());
    }

    //3. 그룹 삭제하기 (DELETE /groups/{id}, 하위 할 일 삭제)
    @DeleteMapping("/{id}")
    public void deleteGroup(
            @PathVariable("id") Long groupId,
            @AuthenticationPrincipal UserDetails user
    ) {
        groupService.deleteGroupForUser(groupId, user.getUsername());
    }

    //4. 그룹 불러오기 (GET /groups/{id})
    //- 그룹 정보 + 하위 할 일(Task) 목록을 함께 반환
    @GetMapping("/{id}")
    public GroupDetailResponse getGroup(
            @PathVariable("id") Long groupId,
            @AuthenticationPrincipal UserDetails user
    ) {
        GroupWithTasksDto result = groupService.getGroupWithTasksForUser(groupId, user.getUsername());

        Group group = result.group();
        List<Task> tasks = result.tasks();

        // Task 엔티티 → 응답용 DTO로 변환
        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getGroup().getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus() != null ? task.getStatus().name() : null,
                        task.getDueDate()
                ))
                .toList();

        return new GroupDetailResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                taskResponses
        );
    }
}
