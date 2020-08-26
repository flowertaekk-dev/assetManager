package com.assetManager.server.api.login.signup;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class SignUpControllerTest {

    @Autowired private MockMvc mvc;

    @Test public void when_user_mail_already_exists() {

    }


}
