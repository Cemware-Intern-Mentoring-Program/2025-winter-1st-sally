package com.cemware.sally.service;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.User;
import com.cemware.sally.repository.GroupRepository;
import com.cemware.sally.repository.TaskRepository;
import com.cemware.sally.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service                    // 스프링이 서비스 빈으로 관리
@Transactional              // 메서드 전체를 하나의 트랜잭션으로 처리
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;

    //1. 유저 생성하기 (1건)
    public Long createUser(String username) {
        // 1) 엔티티 생성
        User user = User.builder()
                .username(username)
                .build();

        // 2) 저장
        User saved = userRepository.save(user);

        // 3) 반환된 전체 엔티티에서 PK(ID)만 꺼내서 반환
        return saved.getId();
    }

    //2. 유저 수정하기 (1건)
    public void updateUser(Long userId, String newUsername) {
        // 1) 유저 조회 (없으면 예외)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));

        // 2) 엔티티 상태 변경
        user.updateUsername(newUsername);
        // @Transactional 덕분에 메서드 종료 시점에 변경 내용 자동 flush (더티 체킹)
    }

    //3. 유저 삭제하기 (1건, 하위 그룹 및 할 일 삭제)
    public void deleteUser(Long userId) {
        // 1) 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));

        // 2) 이 유저가 가진 그룹들 조회
        List<Group> groups = groupRepository.findByUserId(userId);

        // 3) 각 그룹마다 Task 먼저 삭제
        for (Group group : groups) {
            // 그룹에 속한 Task 모두 삭제
            taskRepository.deleteByGroupId(group.getId());
        }

        // 4) 그룹들 삭제
        groupRepository.deleteAll(groups);

        // 5) 마지막으로 유저 삭제
        userRepository.delete(user);
    }

    //4. 유저 불러오기 (1건)
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));
    }

    //5. 유저 그룹 불러오기 (유저가 만든 그룹 전체 불러오기)
    @Transactional
    public List<Group> getUserGroups(Long userId) {
        // 유저가 존재하는지 체크하고 싶다면 한 번 조회 (선택)
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId);
        }

        return groupRepository.findByUserId(userId);
    }
}
