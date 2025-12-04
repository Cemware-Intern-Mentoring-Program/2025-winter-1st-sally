package com.cemware.sally.service;

import com.cemware.sally.domain.User;
import com.cemware.sally.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;      // 가짜 레포지토리

    @InjectMocks
    UserService userService;           // 여기에 mock이 주입됨

    @Test
    void createUser_returns_id() {
        // given: save()가 호출되면 id=1인 User mock을 반환하도록 설정
        User savedUser = mock(User.class);              // ← 여기서 mock 생성
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(savedUser.getId()).thenReturn(1L);         // ← mock 이라서 stub 가능

        // when
        Long id = userService.createUser("tester");

        // then
        assertThat(id).isEqualTo(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_modifies_username() {
        // given: 기존 유저 한 명
        User existing = User.builder()
                .username("old")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        // when
        userService.updateUser(1L, "new-name");

        // then
        assertThat(existing.getUsername()).isEqualTo("new-name");
        verify(userRepository, times(1)).findById(1L);
    }
}
