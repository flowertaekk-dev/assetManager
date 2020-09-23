package com.assetManager.server.controller.setting.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import com.assetManager.server.controller.setting.business.dto.DeleteBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.ReadAllBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.UpdateBusinessRequestDto;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.controller.utils.TestDataUtil;
import com.assetManager.server.domain.business.Business;
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
import java.util.Optional;

@SpringBootTest
public class BusinessControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableCountRepository tableCountRepository;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 유저 데이터 초기화
        UserTestUtil.insertUser();
    }

    @AfterEach
    public void clean() {
        tableCountRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
    }

    // case: 상호명(닉네임)을 등록한다.
    @Test
    public void test_can_save_business_name() throws Exception {
        // given
        String businessName = "testBusiness";

        // when
        ResultActions action = BusinessTestUtil.insertBusinessName(mvc, businessName);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).isNotEmpty();
        assertThat(businesses.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(businesses.get(0).getBusinessName()).isEqualTo(businessName);
    }

    // case: 상호명(닉네임)을 변경한다.
    @Test
    public void test_can_change_business_name() throws Exception {
        // given

        String originalBusinessName = "original";
        String newBusinessName = "newName";

        // 상호명 추가
        BusinessTestUtil.insertBusinessName(mvc, originalBusinessName);

        String businessName = businessRepository.findByUserIdAndBusinessName(TestDataUtil.id, originalBusinessName)
                .orElseThrow()
                .getBusinessName();
        assertThat(businessName).isEqualTo(originalBusinessName);

        // when

        // 상호명 편집
        String content = objectMapper.writeValueAsString(
                UpdateBusinessRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .existingBusinessName(originalBusinessName)
                        .newBusinessName(newBusinessName)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.businessControllerUrl + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        String updatedBusinessName = businessRepository.findByUserIdAndBusinessName(TestDataUtil.id, newBusinessName)
                .orElseThrow()
                .getBusinessName();
        assertThat(updatedBusinessName).isEqualTo(newBusinessName);
    }

    // case: 상호명(닉네임)을 삭제한다.
    @Test
    public void test_can_delete_business_name() throws Exception {
        // given

        String businessName = "businessName";

        {
            ResultActions action = BusinessTestUtil.insertBusinessName(mvc, businessName);
            action.andExpect(status().isOk());
        }

        // when

        String content = objectMapper.writeValueAsString(
                DeleteBusinessRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(businessName)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.businessControllerUrl + "/delete")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        Optional<Business> resultBusiness = businessRepository.findByUserIdAndBusinessName(TestDataUtil.id, businessName);
        assertThat(resultBusiness.isPresent()).isFalse();

    }

    // case: 유저 아이디에 연결된 상호명 전부를 불러올 수 있다.
    @Test
    public void test_can_retrieve_all_business_name_by_userId() throws Exception {
        // given
        String firstBusinessName = "firstBusiness";
        String secondBusinessName = "secondBusinessName";
        String thirdBusinessName = "thirdBusinessName";

        BusinessTestUtil.insertBusinessName(mvc, firstBusinessName);
        BusinessTestUtil.insertBusinessName(mvc, secondBusinessName);
        BusinessTestUtil.insertBusinessName(mvc, thirdBusinessName);

        // when
        String content = objectMapper.writeValueAsString(
                ReadAllBusinessRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.businessControllerUrl + "/readAll")
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

    // TODO case: 상호명이 변경되면 관계가 있는 테이블 및 메뉴의 상호명도 함께 변경된다.

    // TODO case: 상호명이 삭제되면 해당되는 메뉴도 전부 삭제된다.
}
