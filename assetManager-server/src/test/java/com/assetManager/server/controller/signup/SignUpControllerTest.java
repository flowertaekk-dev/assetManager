package com.assetManager.server.controller.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

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

    private String id = "test";
    private String password = "test";
    private String email = "test@email.com";


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
        String url = "/api/v1/signup";

        // when
        String content = objectMapper.writeValueAsString(
                SignUpRequestDto.builder()
                    .id(this.id)
                    .password(this.password)
                    .email(this.email)
                    .build());

        // then
        ResultActions action = mvc.perform(
                post(url)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    // 이미 존재하는 이메일이 있는 경우에 회원가입 실패
    @Test public void cannot_signup_multiple_times_with_the_same_email() throws Exception {
        //given
        String newId = "newTest";
        String newPassword = "newPassword";
        // e-mail은 동일!

        String url = "/api/v1/signup";

        // 기존에 이미 동일한 이메일로 회원가입 완료!
        {
            String content = objectMapper.writeValueAsString(
                    SignUpRequestDto.builder()
                            .id(this.id)
                            .password(this.password)
                            .email(this.email)
                            .build());

            mvc.perform(
                    post(url)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                    .andExpect(jsonPath("$.reason", nullValue()))
                    .andDo(print());
        }

        // when
        String content = objectMapper.writeValueAsString(
                SignUpRequestDto.builder()
                        .id(newId)
                        .password(newPassword)
                        .email(this.email)
                        .build());

        // then
        ResultActions action = mvc.perform(
                post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", is("이미 동일한 이메일로 가입되어 있습니다.")))
                .andDo(print());
    }


}
