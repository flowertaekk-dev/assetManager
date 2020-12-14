package com.assetManager.server.user;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.controller.login.dto.RequestSaltDto;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.domain.user.UserRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.dummy.DummyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;

@ActiveProfiles(profiles = "local")
@SpringBootTest
public class UserLoginTest extends BaseTestUtils {

    // @LocalServerPort private int port;
    // @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;

    @MockBean private JavaMailSender mockMailSender;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // mock javaMailSender
        when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @AfterEach
    public void tearDown() { deleteAllDataBase(); }

    @Test public void can_log_in() throws Exception {
        // given

        dummyCreator.createUser();

        // when
        ResultActions action = mvc.perform(
                post(LOGIN_CONTROLLER_URL)
                        .content(getLoginContent(USER_ID, PASSWORD, EMAIL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andExpect(jsonPath("$.user", notNullValue()))
                .andExpect(jsonPath("$.user.id", notNullValue()))
                .andExpect(jsonPath("$.user.password", nullValue()))
                .andExpect(jsonPath("$.user.salt", nullValue()))
                .andDo(print());
    }

    @Test public void can_hand_over_salt_key_to_client() throws Exception {
        // given
        dummyCreator.createUser();

        // when
        ResultActions action = mvc.perform(
                post(REQUEST_SALT_CONTROLLER_URL)
                        .content(objectMapper.writeValueAsString(
                                RequestSaltDto.builder()
                                        .id(USER_ID)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.salt", is("salt")))
                .andDo(print());

    }

    // TODO case: 로그인 실패 케이스.. 깜빡했다

    // ----------------------------------------------------------------------------------

    private String getLoginContent(String id, String password, String email) throws Exception {
        return objectMapper.writeValueAsString(
                LoginRequestDto.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());
    }
}
