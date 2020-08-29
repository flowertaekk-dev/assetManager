package com.assetManager.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class HelloWorldControllerTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    public void returnGood() throws Exception {
        String healthCheck = "Good!";

        mvc.perform(post("/api/v1/healthcheck"))
                .andExpect(status().isOk())
                .andExpect(content().string(healthCheck));
    }

}
