package com.assetManager.server.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class HelloWorldControllerTest {

    @Autowired private MockMvc mvc;

    @Test
    public void returnGood() throws Exception {
        String healthCheck = "Good!";

        mvc.perform(post("/api/v1/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(content().string(healthCheck));
    }

}
