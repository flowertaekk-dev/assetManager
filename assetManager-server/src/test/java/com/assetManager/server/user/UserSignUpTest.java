package com.assetManager.server.user;

import com.assetManager.server.controller.email.dto.EmailAuthRequestDto;
import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.utils.dummy.DummyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;

@ActiveProfiles(profiles = "local")
@SpringBootTest
@Transactional
public class UserSignUpTest extends BaseTestUtils {

    @Autowired EmailAuthRepository emailAuthRepository;

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private DummyCreator dummyCreator;

    @MockBean private JavaMailSender mockMailSender;

    private MockMvc mvc;

    @BeforeEach public void setUp() {
        deleteAllDataBase();

        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // mock javaMailSender
        when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @AfterEach public void tearDown() {}

    @Test public void can_signup() throws Exception {
        // given

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();

        String emailAuthCode = emailAuthData.get(0).getAuthCode();

        // when

        String content = createContent(USER_ID, SALT, PASSWORD, EMAIL, emailAuthCode);
        ResultActions action = sendSignUpRequest(content);

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    @Test public void cannot_signup_multiple_times_with_the_same_email() throws Exception {
        //given
        dummyCreator.createUser();

        String newId = "newId";
        String newPassword = "newPassword";

        // when
        String content = createContent(newId, SALT, newPassword, EMAIL, RandomString.make());
        ResultActions action = sendSignUpRequest(content);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    @Test public void test_fail_when_receive_wrong_authCode() throws Exception {
        // given
        dummyCreator.createEmailAuth(EmailAuth.EmailAuthStatus.SENT);

        // when
        String wrongAuthCode = "WrongCode";

        String content = createContent(USER_ID, SALT, PASSWORD, EMAIL, wrongAuthCode);
        ResultActions action = sendSignUpRequest(content);

        //then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    @Test public void test_change_emailAuth_status_to_COMPLETED_when_sign_up_succeeded() throws Exception {
        // given

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        Optional<EmailAuth> emailAuthData = emailAuthRepository.findByEmail(EMAIL);
        assertThat(emailAuthData.isPresent()).isTrue();

        String emailAuthCode = emailAuthData.get().getAuthCode();

        // when
        String content = createContent(USER_ID, SALT, PASSWORD, EMAIL, emailAuthCode);
        ResultActions action = sendSignUpRequest(content);

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // then
        EmailAuth emailAuth = emailAuthRepository.findByAuthCode(emailAuthCode);
        assertThat(emailAuth).isNotNull();
        assertThat(emailAuth.getStatus()).isEqualTo(EmailAuth.EmailAuthStatus.COMPLETED);
    }


    // ----------------------------------------------------------------------------

    private String createContent(String id, String salt, String password, String email, String emailAuthCode) throws Exception {
        return objectMapper.writeValueAsString(
                SignUpRequestDto.builder()
                        .id(id)
                        .salt(salt)
                        .password(password)
                        .email(email)
                        .emailAuthCode(emailAuthCode)
                        .build());
    }

    private void sendEmailAuthRequest() throws Exception {
        mvc.perform(
                post(EMAIL_AUTH_CONTROLLER_URL)
                        .content(
                                objectMapper.writeValueAsString(
                                        EmailAuthRequestDto.builder()
                                                .addressTo(TestDataUtil.EMAIL)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    private ResultActions sendSignUpRequest(String content) throws Exception {
        return mvc.perform(
                post(SIGN_UP_CONTROLLER_URL)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }


}
