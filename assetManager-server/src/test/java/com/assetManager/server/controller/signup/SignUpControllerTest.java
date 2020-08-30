package com.assetManager.server.controller.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class SignUpControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private UserRepository userRepository;
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test public void can_signup() throws Exception {
        // given
        String id = "test";
        String password = "test";
        String email = "test@email.com";

        String url = "/api/v1/signup";

        // when
        String content = objectMapper.writeValueAsString(
                SignUpRequestDto.builder()
                    .id(id)
                    .password(password)
                    .email(email)
                    .build());

        // then
        ResultActions action = mvc.perform(
                post(url)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

        action
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"))
                .andDo(print());
    }

    // TODO 이미 존재하는 이메일이 있는 경우에 회원가입 실패하는 케이스 추가하기


}
