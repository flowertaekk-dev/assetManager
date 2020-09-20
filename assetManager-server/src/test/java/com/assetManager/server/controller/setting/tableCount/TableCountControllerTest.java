package com.assetManager.server.controller.setting.tableCount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.assetManager.server.controller.setting.business.dto.AddBusinessRequestDto;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.tableCount.TableCountRepository;
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
public class TableCountControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableCountRepository tableCountRepository;

    private MockMvc mvc;

    private final String id = "test";
    private final String password = "test";
    private final String email = "test@email.com";
    private final String businessName = "AssetManager";

    private final String url = "/api/v1/tableCount";

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        businessRepository.deleteAll();
        tableCountRepository.deleteAll();
    }

    // TODO case: 테이블 카운트 수정하기 (동일하면 수정하지 말자) -> upsert 사용!
    @Test
    public void test_can_upsert_tableCount() {
        // given

        // when

        // then

    }

    // TODO case: userId와 상호명으로 테이블 카운트 불러오기

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

    private ResultActions insertBusinessName(String businessName) throws Exception {
        String content = objectMapper.writeValueAsString(
                AddBusinessRequestDto.builder()
                        .userId(this.id)
                        .businessName(businessName)
                        .build());

        return mvc.perform(
                post(url + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

}
