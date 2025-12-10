package com.cemware.sally.service;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.Task;
import com.cemware.sally.domain.User;
import com.cemware.sally.dto.group.GroupWithTasksDto;
import com.cemware.sally.repository.GroupRepository;
import com.cemware.sally.repository.TaskRepository;
import com.cemware.sally.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    //1. 그룹 생성하기
    //- 어떤 유저(userId)가 name, description으로 새 그룹을 만든다.
    public Long createGroup(Long userId, String name, String description) {
        // 1) 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));

        // 2) Group 엔티티 생성
        Group group = Group.builder()
                .user(user)
                .name(name)
                .description(description)
                .build();

        // 3) 저장 후 반환된 전체 엔티티에서 PK(ID)만 꺼내서 반환
        Group saved = groupRepository.save(group);
        return saved.getId();
    }

    //2. 그룹 이름 수정하기
    public void updateGroupName(Long groupId, String newName) {
        // 1) 그룹 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        // 2) 엔티티 상태 변경
        group.updateName(newName);
        // @Transactional 덕분에 메서드 종료 시점에 자동으로 DB 반영 (더티 체킹)
    }

    //3. 그룹 삭제하기 (아래 할 일task 삭제)
    public void deleteGroup(Long groupId) {
        // 1) 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        // 2) 이 그룹에 속한 Task 전부 삭제
        taskRepository.deleteByGroupId(groupId);

        // 3) 그룹 삭제
        groupRepository.delete(group);
    }

    // 새로 추가: 로그인한 유저의 username으로 그룹 생성
    public Long createGroupByUsername(String username, String name, String description) {
        // 1) username으로 유저 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. username=" + username));

        // 2) Group 엔티티 생성
        Group group = Group.builder()
                .user(user)              // ← 기존 엔티티 그대로 사용
                .name(name)
                .description(description)
                .build();

        // 3) 저장 후 ID 반환
        Group saved = groupRepository.save(group);
        return saved.getId();
    }

    //  로그인한 사용자 기준으로 그룹 이름 수정
    public void updateGroupNameForUser(Long groupId, String newName, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        // 소유자 검증
        if (!group.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 사용자의 그룹은 수정할 수 없습니다.");
        }

        group.updateName(newName);
    }

    // 로그인한 사용자 기준으로 그룹 삭제
    public void deleteGroupForUser(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        if (!group.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 사용자의 그룹은 삭제할 수 없습니다.");
        }

        // 이 그룹에 속한 Task 전부 삭제
        taskRepository.deleteByGroupId(groupId);

        groupRepository.delete(group);
    }


    //4. 그룹 불러오기 (아래 할 일task 함께 불러오기)
    //- 그룹 정보 + 해당 그룹에 속한 Task 목록을 함께 반환
    @Transactional
    public GroupWithTasksDto getGroupWithTasks(Long groupId) {
        // 1) 그룹 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        // 2) 그룹에 속한 Task 목록 조회
        List<Task> tasks = taskRepository.findByGroupId(groupId);

        // 3) 둘을 한 번에 담아서 반환
        return new GroupWithTasksDto(group, tasks);
    }

    // 로그인한 사용자 기준으로 그룹 + 할 일 조회
    @Transactional
    public GroupWithTasksDto getGroupWithTasksForUser(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹이 존재하지 않습니다. id=" + groupId));

        if (!group.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("다른 사용자의 그룹은 조회할 수 없습니다.");
        }

        List<Task> tasks = taskRepository.findByGroupId(groupId);

        return new GroupWithTasksDto(group, tasks);
    }
}
