package com.assetManager.server.controller.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
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

    private String id = "test";
    private String password = "test";
    private String email = "flowertaekk.dev@gmail.com";

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
        createUserAs(User.UserStatus.USING);

        String url = "/api/v1/login";

        // when
        ResultActions action = mvc.perform(
                post(url)
                        .content(getLoginContent(id, password, email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    private void createUserAs(User.UserStatus userStatus) {
        userRepository.save(
                User.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .status(userStatus)
                        .build());
    }

    private String getLoginContent(String id, String password, String email) throws Exception {
        return objectMapper.writeValueAsString(
                LoginRequestDto.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());
    }

}
