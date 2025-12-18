package com.cemware.sally.repository;

import com.cemware.sally.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional   // 각 테스트 끝나면 롤백
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save_and_find_by_id() {
        // given
        User user = User.builder()
                .username("hyunji")
                .build();

        // when
        User saved = userRepository.save(user);
        User found = userRepository.findById(saved.getId())
                .orElseThrow();

        // then
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getUsername()).isEqualTo("hyunji");
    }
}
