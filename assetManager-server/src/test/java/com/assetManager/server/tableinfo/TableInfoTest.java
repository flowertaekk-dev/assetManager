package com.assetManager.server.tableinfo;

import com.assetManager.server.controller.setting.table.dto.UpdateTableCountRequestDto;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.utils.dummy.DummyCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;

@ActiveProfiles(profiles = "local")
@SpringBootTest
public class TableInfoTest extends BaseTestUtils {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TableInfoRepository tableInfoRepository;

    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;

    private final String businessName = "AssetManager";

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
        dummyCreator.createUser();
    }

    @AfterEach
    public void clean() { deleteAllDataBase(); }

    // case: 테이블 정보 추가하기
    @Test public void test_can_save_tableInfo() throws Exception {
        // given
        int tableCount = 10;
        Business business = dummyCreator.createBusiness();

        // when
        ResultActions action = addTableInfo.apply(business.getBusinessId(), tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<TableInfo> tableInfos = tableInfoRepository.findAll();
        assertThat(tableInfos).isNotEmpty();
        assertThat(tableInfos.get(0).getTableInfoId()).isNotNull();
        assertThat(tableInfos.get(0).getUserId()).isEqualTo(TestDataUtil.USER_ID);
        assertThat(tableInfos.get(0).getBusinessId()).isEqualTo(business.getBusinessId());
        assertThat(tableInfos.get(0).getTableCount()).isEqualTo(tableCount);
    }

    // case: 동일한 상호명에 이미 테이블 정보가 존재하면 실패
    @Test public void test_fail_if_the_same_tableInfo_already_exists() throws Exception {
        // given
        int tableCount = 10;
        TableInfo tableInfo = dummyCreator.createTableInfo(tableCount);

        List<TableInfo> tableInfos = tableInfoRepository.findAll();
        assertThat(tableInfos).isNotEmpty();
        assertThat(tableInfos.get(0).getTableInfoId()).isNotNull();
        assertThat(tableInfos.get(0).getUserId()).isEqualTo(USER_ID);
        assertThat(tableInfos.get(0).getBusinessId()).isEqualTo(tableInfo.getBusinessId());
        assertThat(tableInfos.get(0).getTableCount()).isEqualTo(tableCount);

        // when
        ResultActions action = addTableInfo.apply(tableInfo.getBusinessId(), tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: 테이블 카운트 수정하기
    @Test public void test_can_update_tableCount() throws Exception {
        // given
        int tableCount = 10;
        Business business = dummyCreator.createBusiness();

        {
            ResultActions action = addTableInfo.apply(business.getBusinessId(), tableCount);
            action
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                    .andExpect(jsonPath("$.reason", nullValue()))
                    .andDo(print());

            List<TableInfo> tableInfos = tableInfoRepository.findAll();
            assertThat(tableInfos).isNotEmpty();
            assertThat(tableInfos.get(0).getTableInfoId()).isNotNull();
            assertThat(tableInfos.get(0).getUserId()).isEqualTo(TestDataUtil.USER_ID);
            assertThat(tableInfos.get(0).getBusinessId()).isEqualTo(business.getBusinessId());
            assertThat(tableInfos.get(0).getTableCount()).isEqualTo(10);
        }

        // when
        int secondTableCount = 20;
        ResultActions action = addTableInfo.apply(business.getBusinessId(), secondTableCount);

        String content = objectMapper.writeValueAsString(
                UpdateTableCountRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(business.getBusinessId())
                        .tableCount(secondTableCount)
                        .build());

        ResultActions secondAction = mvc.perform(
                post(TestDataUtil.TABLE_INFO_CONTROLLER_URL + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        secondAction
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // then
        List<TableInfo> secondTableInfos = tableInfoRepository.findAll();
        assertThat(secondTableInfos).isNotEmpty();
        assertThat(secondTableInfos.get(0).getUserId()).isEqualTo(TestDataUtil.USER_ID);
        assertThat(secondTableInfos.get(0).getBusinessId()).isEqualTo(business.getBusinessId());
        assertThat(secondTableInfos.get(0).getTableCount()).isEqualTo(secondTableCount);
    }

    // case: 상호명이 존재하지 않으면 테이블 카운트 생성 실패 ( 정상적인 상황으로는 일어나지 않을지도 )
    @Test
    public void test_cannot_save_tableCount_when_businessName_does_not_exist() throws Exception {
        // given
        int tableCount = 10;
        dummyCreator.createBusiness();
        String fakeBusinessId = "BS-00000000-00000000";

        // when
        ResultActions action = addTableInfo.apply(fakeBusinessId, tableCount);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: userId와 상호명으로 테이블 카운트 불러오기
    @Test public void test_can_retrieve_tableCount_by_userId_and_businessId() throws Exception {
        // given
        int tableCount = 20;

        Business business = dummyCreator.createBusiness();
        addTableInfo.apply(business.getBusinessId(), tableCount);

        // when
        String content = objectMapper.writeValueAsString(
                UpdateTableCountRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(business.getBusinessId())
                        .tableCount(tableCount)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.TABLE_INFO_CONTROLLER_URL + "/read")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(TestDataUtil.USER_ID)))
                .andExpect(jsonPath("$.businessId", is(business.getBusinessId())))
                .andExpect(jsonPath("$.tableCount", is(tableCount)))
                .andDo(print());

    }

    // -----------------------------------------------------------------------

    private BiFunction<String, Integer, ResultActions> addTableInfo = (businessId, tableCount) -> {
        try {
            String content = objectMapper.writeValueAsString(
                    UpdateTableCountRequestDto.builder()
                            .userId(TestDataUtil.USER_ID)
                            .businessId(businessId)
                            .tableCount(tableCount)
                            .build());

            return mvc.perform(
                    post(TestDataUtil.TABLE_INFO_CONTROLLER_URL + "/add")
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON));
        } catch (Exception ex){
            fail("테이블정보 테스트 데이터 생성 실패");
        }

        return null;
    };

}
