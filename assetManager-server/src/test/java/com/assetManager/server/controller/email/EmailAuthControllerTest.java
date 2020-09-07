package com.assetManager.server.controller.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.assetManager.server.controller.email.dto.EmailAuthRequestDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.user.User;
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

import java.util.List;

@SpringBootTest
public class EmailAuthControllerTest {

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EmailAuthRepository emailAuthRepository;
    @Autowired private UserRepository userRepository;

    private MockMvc mvc;

    private final String email = "test@test.com";
    private final String url = "/api/v1/auth/email";

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void clean() {
        emailAuthRepository.deleteAll();
    }

    @Test
    public void test_can_save_emailAuth() throws Exception {
        // given
        String email = "test@test.com";

        String url = "/api/v1/auth/email";

        // when
        ResultActions action = mvc.perform(
                post(url)
                        .content(
                                objectMapper.writeValueAsString(
                                    EmailAuthRequestDto.builder()
                                        .addressTo(email)
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();
        assertThat(emailAuthData.get(0).getEmail()).isEqualTo(email);
        assertThat(emailAuthData.get(0).getStatus()).isEqualTo(EmailAuth.EmailAuthStatus.SENT);
    }
    // TODO case: 유저가 입력한 이메일이 이미 등록이 되어있는 이메일이라면 인증코드 발급 실패한다.
    @Test
    public void test_cannot_use_registered_email() {
        // given
        userRepository.save(
                User.builder()
                        .id("test")
                        .password("test")
                        .email(email)
                        .status(User.UserStatus.APPLIED)
                        .build());


        // when

        // then
    }


    // TODO case: 동일한 이메일로 두 번 요청이 들어왔을 때 마지막 코드가 디비에 들어있어서 마지막 코드가 아니면 인증 실패한다.

}
