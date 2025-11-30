package com.cemware.sally.controller;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.User;
import com.cemware.sally.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController                     // 이 클래스가 REST API 컨트롤러라는 뜻
@RequestMapping("/users")           // 모든 메서드의 기본 URL 앞부분: /users
@RequiredArgsConstructor            // final 필드를 이용한 생성자 자동 생성 (UserService 주입용)
public class UserController {

    private final UserService userService;   // 서비스 계층 의존

    //1. 유저 생성하기 (POST /users)
    @PostMapping
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
        // 서비스 호출해서 유저 생성
        Long userId = userService.createUser(request.getUsername());
        // 생성된 유저의 id를 응답 DTO로 감싸서 반환
        return new CreateUserResponse(userId);
    }

    //2. 유저 수정하기 (PUT /users/{id})
    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") Long userId,
                           @RequestBody UpdateUserRequest request) {
        // 경로에서 받은 id와, body에서 받은 username으로 수정
        userService.updateUser(userId, request.getUsername());
    }

    //3. 유저 삭제하기 (DELETE /users/{id}, 하위 그룹 및 할 일 삭제)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
    }

    //4. 유저 불러오기 (GET /users/{id})
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Long userId) {
        User user = userService.getUser(userId);
        return new UserResponse(user.getId(), user.getUsername());
    }

    //5. 유저 그룹 불러오기 (GET /users/{id}/groups)
    @GetMapping("/{id}/groups")
    public List<GroupResponse> getUserGroups(@PathVariable("id") Long userId) {
        List<Group> groups = userService.getUserGroups(userId);

        // 엔티티 Group → 응답용 DTO로 변환
        return groups.stream()
                .map(g -> new GroupResponse(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());
    }

    /* ===== 요청/응답에 사용할 DTO 클래스들 ===== */

    // 유저 생성 요청 바디: { "username": "홍길동" }
    public static class CreateUserRequest {
        private String username;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    }

    // 유저 수정 요청 바디: { "username": "새이름" }
    public static class UpdateUserRequest {
        private String username;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    }

    // 유저 생성 응답: { "id": 1 }
    public static class CreateUserResponse {
        private Long id;

        public CreateUserResponse(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    // 유저 조회 응답: { "id": 1, "username": "홍길동" }
    public static class UserResponse {
        private Long id;
        private String username;

        public UserResponse(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Long getId() {
            return id;
        }
        public String getUsername() {
            return username;
        }
    }

    // 유저 그룹 목록 응답 DTO
    public static class GroupResponse {
        private Long id;
        private String name;
        private String description;

        public GroupResponse(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
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
    }

    //컨트롤러 연결 확인용
    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

}
