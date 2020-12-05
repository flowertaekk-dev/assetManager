package com.assetManager.server.controller.updateUser;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.controller.updateUser.dto.UpdateUserRequestDto;
import com.assetManager.server.controller.updateUser.dto.UpdateUserResponseDto;
import com.assetManager.server.controller.utils.TestDataUtil;
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
public class UpdateUserControllerTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UpdateUserController updateUserController;
    @Autowired private UpdateUserService updateUserService;
    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;
    private String newPassword = "helloworld";

    @BeforeEach public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        UserTestUtil.insertUser();
    }

    @AfterEach public void tearDown() {
        userRepository.deleteAll();
    }

    // 패스워드 업데이트가 잘 이루어진다
    @Test public void test_can_update_password_through_api() throws Exception {
        // given
        UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
                .id(TestDataUtil.id)
                .email(TestDataUtil.email)
                .updatingPassword(newPassword)
                .build();

        // when
        ResultActions action = mvc.perform(
                post(TestDataUtil.updateUserControllerUrl)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus",is("SUCCESS")))
                .andExpect(jsonPath("$.user.password", is(newPassword)))
                .andDo(print());

        // DB 데이터 확인
        User resultUser = userRepository.findByIdAndEmail(TestDataUtil.id, TestDataUtil.email)
                .orElseThrow();

        assertThat(resultUser.getPassword()).isEqualTo(newPassword);
    }

}
