package com.assetManager.server.controller.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private WebApplicationContext context;
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

    @Test
    public void can_log_in() throws Exception {
        // given
        String id = "test";
        String password = "test";
        String email = "test@test.com";

        String url = "/api/v1/login";

        userRepository.save(
                User.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .status(User.UserStatus.USING)
                        .build());
        // when
        String content = objectMapper.writeValueAsString(
                LoginRequestDto.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());

        ResultActions action = mvc.perform(
                post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));


        // then
        action
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"))
                .andDo(print());
    }

}
