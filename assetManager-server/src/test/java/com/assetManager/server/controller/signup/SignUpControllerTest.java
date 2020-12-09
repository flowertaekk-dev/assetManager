package com.assetManager.server.controller.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import com.assetManager.server.controller.email.dto.EmailAuthRequestDto;
import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.List;

@SpringBootTest
public class SignUpControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailAuthRepository emailAuthRepository;

    @MockBean private JavaMailSender mockMailSender;

    private MockMvc mvc;

    private final String id = "test";
    private final String salt = "salt";
    private final String password = "test";
    private final String email = "test@email.com";

    private final String url = "/api/v1/signup";


    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // mock javaMailSender
        when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        emailAuthRepository.deleteAll();
    }

    @Test public void can_signup() throws Exception {
        // given

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();

        String emailAuthCode = emailAuthData.get(0).getAuthCode();

        // when

        String content = createContent(this.id, this.salt, this.password, this.email, emailAuthCode);;
        ResultActions action = sendRequest(content);

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    // case: 이미 존재하는 이메일이 있는 경우에 회원가입 실패
    @Test public void cannot_signup_multiple_times_with_the_same_email() throws Exception {
        //given
        String newId = "newTest";
        String newPassword = "newPassword";

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();

        String emailAuthCode = emailAuthData.get(0).getAuthCode();

        // 기존에 이미 동일한 이메일로 회원가입 완료!
        {
            String content = createContent(this.id, this.salt, this.password, this.email, emailAuthCode);
            sendRequest(content);
        }

        // when
        String content = createContent(newId, this.salt, newPassword, this.email, emailAuthCode);
        ResultActions action = sendRequest(content);

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: EmailAuthCode가 틀리면 회원가입 실패한다
    @Test public void test_fail_when_receive_wrong_authCode() throws Exception {
        // given

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();

        String emailAuthCode = emailAuthData.get(0).getAuthCode();

        // when

        String wrongAuthCode = "WrongCode";

        String content = createContent(this.id, this.salt, this.password, this.email, wrongAuthCode);
        ResultActions action = sendRequest(content);

        //then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: signup 성공시 emailAuth status를 '인증완료'로 갱신한다
    @Test public void test_change_emailAuth_status_to_COMPLETED_when_sign_up_succeeded() throws Exception {
        // given

        // 이메일 인증코드를 발급받는다
        sendEmailAuthRequest();

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();

        String emailAuthCode = emailAuthData.get(0).getAuthCode();

        // when
        String content = createContent(this.id, this.salt, this.password, this.email, emailAuthCode);
        ResultActions action = sendRequest(content);

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

    // ----------------------------------------------------------------
    // utils

    protected String createContent(String id, String salt, String password, String email, String emailAuthCode) throws Exception {
        return objectMapper.writeValueAsString(
                SignUpRequestDto.builder()
                        .id(id)
                        .salt(salt)
                        .password(password)
                        .email(email)
                        .emailAuthCode(emailAuthCode)
                        .build());
    }

    protected ResultActions sendRequest(String content) throws Exception {
        return mvc.perform(
                post(this.url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

    private void sendEmailAuthRequest() throws Exception {
        mvc.perform(
                post("/api/v1/email/requestCode")
                        .content(
                                objectMapper.writeValueAsString(
                                        EmailAuthRequestDto.builder()
                                                .addressTo(this.email)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }
}
