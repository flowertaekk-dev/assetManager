package com.assetManager.server.controller.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
public class SignUpControllerTest {

    // TODO code here
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Test public void can_signup() {
        // given
        String id = "test";
        String password = "test";
        String email = "test@email.com";

        String url = "/api/v1/signup";

        // when
//        String content = objectMapper.writeValueAsString(
//                User)

        // then
    }


}
