package com.cemware.sally.controller;

import com.cemware.sally.domain.Group;
import com.cemware.sally.domain.User;
import com.cemware.sally.dto.group.GroupResponse;
import com.cemware.sally.dto.user.CreateUserRequest;
import com.cemware.sally.dto.user.CreateUserResponse;
import com.cemware.sally.dto.user.UpdateUserRequest;
import com.cemware.sally.dto.user.UserResponse;
import com.cemware.sally.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController                     // 이 클래스가 REST API 컨트롤러라는 뜻
@RequestMapping("/users")           // 모든 메서드의 기본 URL 앞부분: /users
@RequiredArgsConstructor            // final 필드를 이용한 생성자 자동 생성 (UserService 주입용)
public class UserController {

    private final UserService userService;   // 서비스 계층 의존

//    //1. 유저 생성하기 (POST /users)
//    @PostMapping
//    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {
//        // 서비스 호출해서 유저 생성
//        Long userId = userService.createUser(request.username());
//        // 생성된 유저의 id를 응답 DTO로 감싸서 반환
//        return new CreateUserResponse(userId);
//    }

    //2. 유저 수정하기 (PUT /users/{id})
    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") Long userId,
                           @RequestBody UpdateUserRequest request,
                           @AuthenticationPrincipal UserDetails user) {
        // 경로에서 받은 id와, body에서 받은 username으로 수정
        userService.updateUserForUser(userId, request.username(), user.getUsername());
    }

    //3. 유저 삭제하기 (DELETE /users/{id}, 하위 그룹 및 할 일 삭제)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId,
                           @AuthenticationPrincipal UserDetails user) {
        userService.deleteUserForUser(userId,  user.getUsername());
    }

    //4. 유저 불러오기 (GET /users/{id})
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable("id") Long userId,
                                @AuthenticationPrincipal UserDetails user) {
        User found = userService.getUserForUser(userId, user.getUsername());
        return new UserResponse(found.getId(), found.getUsername());
    }

    //5. 유저 그룹 불러오기 (GET /users/{id}/groups)
    @GetMapping("/{id}/groups")
    public List<GroupResponse> getUserGroups(@PathVariable("id") Long userId,
                                             @AuthenticationPrincipal UserDetails user) {
        List<Group> groups = userService.getUserGroupsForUser(userId, user.getUsername());

        // 엔티티 Group → 응답용 DTO로 변환
        return groups.stream()
                .map(g -> new GroupResponse(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());
    }

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserDetails user) {
        return "current user = " + user.getUsername();
    }

}
