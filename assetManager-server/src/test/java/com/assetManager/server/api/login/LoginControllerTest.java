package com.assetManager.server.api.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import com.assetManager.server.domain.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = LoginController.class,
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
public class LoginControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void can_log_in() throws Exception {
        // given
        String id = "test";
        String password = "test";
        String email = "test@test.com";

        String url = "/api/v1/login";

        // when

        String content = objectMapper.writeValueAsString(User.builder()
            .id(id)
            .password(password)
            .email(email)
            .build());

        // then
        ResultActions result = mvc.perform(
                post(url)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"))
                .andDo(print());
    }

}
