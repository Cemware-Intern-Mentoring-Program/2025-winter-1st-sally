package com.cemware.sally.controller;

import com.cemware.sally.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean   // 컨트롤러가 의존하는 서비스는 mock 으로 대체
    UserService userService;

    @Test
    void create_user_returns_id() throws Exception {
        // given
        given(userService.createUser("tester")).willReturn(1L);

        // when & then
        mockMvc.perform(
                        post("/users")
                                .contentType("application/json")
                                .content("{\"username\":\"tester\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
