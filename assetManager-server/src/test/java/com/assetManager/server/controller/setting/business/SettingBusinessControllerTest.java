package com.assetManager.server.controller.setting.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;


import com.assetManager.server.controller.setting.business.dto.SettingBusinessRequestDto;
import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class SettingBusinessControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;

    private MockMvc mvc;

    private final String id = "test";
    private final String password = "test";
    private final String email = "test@email.com";

    private final String url = "/api/v1/business";

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 유저 데이터 초기화
        insertUser();
    }

    @AfterEach
    public void clean() {
        businessRepository.deleteAll();
        userRepository.deleteAll();
    }

    // case: 상호명(닉네임)을 등록한다.
    @Test
    public void test_can_save_business_name() throws Exception {
        // given

        String businessName = "testBusiness";

        String content = objectMapper.writeValueAsString(
                SettingBusinessRequestDto.builder()
                        .userId(this.id)
                        .businessName(businessName)
                        .build());;

        // when

        ResultActions action = mvc.perform(
                post(url + "/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    // TODO case: 상호명(닉네임)을 변경한다.

    // TODO case: 상호명(닉네임)을 삭제한다.

    // TODO case: 유저 아이디에 연결된 상호명 전부를 불러올 수 있다.

    // --------------------------------------------------------------------
    // utils

    private void insertUser() {
        userRepository.save(
                User.builder()
                        .id(this.id)
                        .password(this.password)
                        .email(this.email)
                        .status(User.UserStatus.USING)
                        .build());
    }

}
