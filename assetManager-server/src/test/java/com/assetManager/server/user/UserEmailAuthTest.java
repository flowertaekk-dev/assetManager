package com.assetManager.server.user;

import com.assetManager.server.controller.email.dto.EmailAuthRequestDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.TestDataUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.List;

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
public class UserEmailAuthTest extends BaseTestUtils {

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EmailAuthRepository emailAuthRepository;
    @Autowired private UserRepository userRepository;

    @Autowired private DummyCreator dummyCreator;

    @MockBean
    private JavaMailSender mockMailSender;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        deleteAllDataBase();

        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // mock javaMailSender
        when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @AfterEach
    public void tearDown() {}

    // case: EmailAuth 테이블에 데이터를 등록할 수 있다.
    @Test public void test_can_save_emailAuth() throws Exception {
        // given

        // when
        ResultActions action = sendRequest();

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isNotEmpty();
        assertThat(emailAuthData.get(0).getEmail()).isEqualTo(EMAIL);
        assertThat(emailAuthData.get(0).getStatus()).isEqualTo(EmailAuth.EmailAuthStatus.SENT);
    }

    // case: 유저가 입력한 이메일이 이미 등록이 되어있는 이메일이라면 인증코드 발급 실패한다.
    @Test public void test_fail_with_registered_email() throws Exception {
        // given

        // 기존 유저용 데이터 생성
        dummyCreator.createUser();

        // when
        ResultActions action = sendRequest();

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());

        List<EmailAuth> emailAuthData = emailAuthRepository.findAll();
        assertThat(emailAuthData).isEmpty();
    }

    // case: 동일한 이메일로 두 번 요청이 들어왔을 때 마지막 코드가 디비에 들어있어서 마지막 코드가 아니면 인증 실패한다.
    @Test public void test_the_last_auth_code_is_used_to_authenticate_email() throws Exception {
        // given

        // when

        // 첫 리퀘스트
        sendRequest();

        String firstAuthCode =
                emailAuthRepository.findByEmail(EMAIL)
                        .orElseThrow()
                        .getAuthCode();

        // 두 번째 리퀘스트
        ResultActions action = sendRequest();

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        String secondAuthCode =
                emailAuthRepository.findByEmail(EMAIL)
                        .orElseThrow()
                        .getAuthCode();

        // then
        assertThat(firstAuthCode).isNotEqualTo(secondAuthCode);
    }

    // ----------------------------------------------------------------

    private ResultActions sendRequest() throws Exception {
        return mvc.perform(
                post(TestDataUtil.EMAIL_AUTH_CONTROLLER_URL)
                        .content(
                                objectMapper.writeValueAsString(
                                        EmailAuthRequestDto.builder()
                                                .addressTo(TestDataUtil.EMAIL)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

}
