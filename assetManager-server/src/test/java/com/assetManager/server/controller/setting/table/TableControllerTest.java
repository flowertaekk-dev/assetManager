package com.assetManager.server.controller.setting.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import com.assetManager.server.controller.setting.business.BusinessTestUtil;
import com.assetManager.server.controller.setting.table.dto.UpdateTableCountRequestDto;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.controller.utils.TestDataUtil;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.tableCount.TableCount;
import com.assetManager.server.domain.tableCount.TableCountRepository;
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

import java.util.List;

@SpringBootTest
public class TableControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableCountRepository tableCountRepository;

    private MockMvc mvc;

    private final String businessName = "AssetManager";

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 유저 정보 생성
        UserTestUtil.insertUser();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        businessRepository.deleteAll();
        tableCountRepository.deleteAll();
    }

    // case: 테이블 카운트 추가하기
    @Test
    public void test_can_save_tableCount() throws Exception {
        // given
        int tableCount = 10;
        BusinessTestUtil.insertBusinessName(mvc, this.businessName);

        // when
        ResultActions action = TableTestUtil.insertTableCount(mvc, businessName, tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<TableCount> tableCounts = tableCountRepository.findAll();
        assertThat(tableCounts).isNotEmpty();
        assertThat(tableCounts.get(0).getTableCountId()).isNotNull();
        assertThat(tableCounts.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(tableCounts.get(0).getBusinessName()).isEqualTo(businessName);
        assertThat(tableCounts.get(0).getTableCount()).isEqualTo(10);
    }

    // case: 동일한 상호명에 테이블 카운트가 이미 존재하면 실패
    @Test
    public void test_fail_if_the_same_tableCount_already_exists() throws Exception {
        // given
        int tableCount = 10;
        BusinessTestUtil.insertBusinessName(mvc, this.businessName);

        TableTestUtil.insertTableCount(mvc, this.businessName, tableCount)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<TableCount> tableCounts = tableCountRepository.findAll();
        assertThat(tableCounts).isNotEmpty();
        assertThat(tableCounts.get(0).getTableCountId()).isNotNull();
        assertThat(tableCounts.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(tableCounts.get(0).getBusinessName()).isEqualTo(this.businessName);
        assertThat(tableCounts.get(0).getTableCount()).isEqualTo(10);

        // when
        ResultActions action = TableTestUtil.insertTableCount(mvc, this.businessName, tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", is("테이블 정보가 이미 존재합니다.")))
                .andDo(print());
    }

    // case: 테이블 카운트 수정하기
    @Test
    public void test_can_update_tableCount() throws Exception {
        // given
        int tableCount = 10;
        BusinessTestUtil.insertBusinessName(mvc, this.businessName);

        // when
        TableTestUtil.insertTableCount(mvc, businessName, tableCount)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<TableCount> tableCounts = tableCountRepository.findAll();
        assertThat(tableCounts).isNotEmpty();
        assertThat(tableCounts.get(0).getTableCountId()).isNotNull();
        assertThat(tableCounts.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(tableCounts.get(0).getBusinessName()).isEqualTo(businessName);
        assertThat(tableCounts.get(0).getTableCount()).isEqualTo(10);

        // then
        // 수정
        int secondTableCount = 20;
        String content = objectMapper.writeValueAsString(
                UpdateTableCountRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .tableCount(secondTableCount)
                        .build());

        ResultActions secondAction = mvc.perform(
                post(TestDataUtil.tableCountControllerUrl + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        secondAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<TableCount> secondTableCounts = tableCountRepository.findAll();
        assertThat(secondTableCounts).isNotEmpty();
        assertThat(secondTableCounts.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(secondTableCounts.get(0).getBusinessName()).isEqualTo(this.businessName);
        assertThat(secondTableCounts.get(0).getTableCount()).isEqualTo(secondTableCount);
    }

    // case: 상호명이 존재하지 않으면 테이블 카운트 생성 실패 ( 정상적인 상황으로는 일어나지 않을지도 )
    @Test
    public void test_cannot_save_tableCount_when_businessName_does_not_exist() throws Exception {
        // given
        int tableCount = 10;

        // when
        ResultActions action = TableTestUtil.insertTableCount(mvc, businessName, tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", is("상호명(닉네임)이 존재하지 않습니다.")))
                .andDo(print());

    }

    // case: userId와 상호명으로 테이블 카운트 불러오기
    @Test
    public void test_can_retrieve_tableCount_by_userId_and_businessName() throws Exception {
        // given
        int tableCount = 20;

        BusinessTestUtil.insertBusinessName(mvc, businessName);
        TableTestUtil.insertTableCount(mvc, businessName, tableCount);

        // when
        String content = objectMapper.writeValueAsString(
                UpdateTableCountRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(businessName)
                        .tableCount(tableCount)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.tableCountControllerUrl + "/read")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(TestDataUtil.id)))
                .andExpect(jsonPath("$.businessName", is(businessName)))
                .andExpect(jsonPath("$.tableCount", is(tableCount)))
                .andDo(print());

    }

}
