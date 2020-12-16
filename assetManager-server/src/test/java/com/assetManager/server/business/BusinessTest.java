package com.assetManager.server.business;

import com.assetManager.server.controller.setting.business.dto.AddBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.DeleteBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.ReadAllBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.UpdateBusinessRequestDto;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.RandomIdCreator;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;

@ActiveProfiles(profiles = "local")
@SpringBootTest
@Transactional
public class BusinessTest extends BaseTestUtils {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;

    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        dummyCreator.createUser();
    }

    @AfterEach
    public void tearDown() { deleteAllDataBase(); }

    // case: 상호명(닉네임)을 등록한다.
    @Test public void test_can_save_business_name() throws Exception {
        // given

        // when
        String content = objectMapper.writeValueAsString(
                AddBusinessRequestDto.builder()
                        .userId(USER_ID)
                        .businessName(BUSINESS)
                        .build());

        ResultActions action = mvc.perform(
                post(BUSINESS_CONTROLLER_URL + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).isNotEmpty();
        assertThat(businesses.get(0).getUserId()).isEqualTo(USER_ID);
        assertThat(businesses.get(0).getBusinessName()).isEqualTo(BUSINESS);
    }

    // case: 상호명(닉네임)을 변경한다.
    @Test public void test_can_change_business_name() throws Exception {
        // given
        String newBusinessName = "newName";

        // 상호명 추가
        dummyCreator.createBusiness();

        String businessName = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, BUSINESS)
                .orElseThrow()
                .getBusinessName();
        assertThat(businessName).isEqualTo(BUSINESS);

        // when
        // 상호명 수정
        String content = objectMapper.writeValueAsString(
                UpdateBusinessRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .existingBusinessName(BUSINESS)
                        .newBusinessName(newBusinessName)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.BUSINESS_CONTROLLER_URL + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        String updatedBusinessName = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, newBusinessName)
                .orElseThrow()
                .getBusinessName();
        assertThat(updatedBusinessName).isEqualTo(newBusinessName);
    }

    // case: 상호명(닉네임)을 삭제한다.
    @Test public void test_can_delete_business_name() throws Exception {
        // given
        dummyCreator.createBusiness();

        // when
        String content = objectMapper.writeValueAsString(
                DeleteBusinessRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessName(BUSINESS)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.BUSINESS_CONTROLLER_URL + "/delete")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        Optional<Business> resultBusiness = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, BUSINESS);
        assertThat(resultBusiness.isPresent()).isFalse();

    }

    // case: 유저 아이디에 연결된 상호명 전부를 불러올 수 있다.
    @Test
    public void test_can_retrieve_all_business_name_by_userId() throws Exception {
        // given
        String firstBusinessName = "firstBusiness";
        String secondBusinessName = "secondBusinessName";
        String thirdBusinessName = "thirdBusinessName";

        createBusiness(firstBusinessName);
        createBusiness(secondBusinessName);
        createBusiness(thirdBusinessName);

        // when
        String content = objectMapper.writeValueAsString(
                ReadAllBusinessRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.BUSINESS_CONTROLLER_URL + "/readAll")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.businessNames[0].businessName", is(firstBusinessName)))
                .andExpect(jsonPath("$.businessNames[1].businessName", is(secondBusinessName)))
                .andExpect(jsonPath("$.businessNames[2].businessName", is(thirdBusinessName)))
                .andDo(print());
    }

    // case: 상호명이 삭제되면 해당되는 테이블 정보도 전부 삭제된다.
    @Test public void test_when_businessName_is_deleted_tableInfo_is_also_deleted() throws Exception {
        // given
        TableInfo tableInfo = dummyCreator.createTableInfo(2);

        // when
        String content = objectMapper.writeValueAsString(
                DeleteBusinessRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessName(BUSINESS)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.BUSINESS_CONTROLLER_URL + "/delete")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // then
        Optional<TableInfo> tableAfterDeletingBusiness =
                tableInfoRepository.findByUserIdAndBusinessId(TestDataUtil.USER_ID, tableInfo.getBusinessId());

        assertThat(tableAfterDeletingBusiness.isEmpty()).isTrue();
    }

    // case: 상호명이 삭제되면 해당되는 메뉴도 전부 삭제된다.
    @Test public void test_when_businessName_is_deleted_menus_are_also_deleted() throws Exception {
        // given
        String menuName = "비빔냉면";
        int price = 5000;
        Menu menu = dummyCreator.createMenu(menuName, price);

        // when
        String content = objectMapper.writeValueAsString(
                DeleteBusinessRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessName(BUSINESS)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.BUSINESS_CONTROLLER_URL + "/delete")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // then
        Optional<Menu> menuAfterDeletingBusiness =
                menuRepository.findByUserIdAndBusinessIdAndMenu(TestDataUtil.USER_ID, menu.getBusinessId(), menuName);

        assertThat(menuAfterDeletingBusiness.isEmpty()).isTrue();
    }

    // --------------------------------------------------------------------------------------------

    private Business createBusiness(String businessName) {
        return businessRepository.save(
                Business.builder()
                        .userId(USER_ID)
                        .businessId(RandomIdCreator.createBusinessId())
                        .businessName(businessName)
                        .build());
    }


}
