package com.assetManager.server.controller.user;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.assetManager.server.controller.setting.business.BusinessTestUtil;
import com.assetManager.server.controller.setting.menu.MenuTestUtil;
import com.assetManager.server.controller.setting.table.TableTestUtil;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.controller.user.dto.DeleteUserRequestDto;
import com.assetManager.server.controller.user.dto.UpdateUserRequestDto;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
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

@ActiveProfiles(profiles = "dev")
@SpringBootTest
public class UserControllerTest {

    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private UserController userController;
    @Autowired private UserService userService;
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
        businessRepository.deleteAll();
        tableInfoRepository.deleteAll();
        menuRepository.deleteAll();
    }

    // 패스워드 업데이트가 잘 이루어진다
    @Test public void test_can_update_password_through_api() throws Exception {
        // given
        UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
                .id(TestDataUtil.USER_ID)
                .email(TestDataUtil.EMAIL)
                .updatingPassword(newPassword)
                .build();

        // when
        ResultActions action = mvc.perform(
                post(TestDataUtil.UPDATE_USER_CONTROLLER_URL)
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
        User resultUser = userRepository.findByIdAndEmail(TestDataUtil.USER_ID, TestDataUtil.EMAIL)
                .orElseThrow();

        assertThat(resultUser.getPassword()).isEqualTo(newPassword);
    }


    @Test public void test_can_delete_user_data_with_related_data() throws Exception {
        // given
        String businessName = "assetManager";
        int tableCount = 3;
        String menu = "testMenu";
        int price = 2000;

        // business 생성
        BusinessTestUtil.insertBusinessName(mvc, businessName);
        String businessId = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, businessName)
                .orElseThrow()
                .getBusinessId();

        // tableInfo 생성
        TableTestUtil.insertTableInfo(mvc, businessId, tableCount);
        List<TableInfo> tables = tableInfoRepository.findAll();
        TableInfo table = tables.stream()
                .findFirst()
                .orElseThrow();
        assertThat(table.getTableCount()).isEqualTo(tableCount);

        // menu 생성
        MenuTestUtil.insertTableCount(this.mvc, businessId, menu, price);
        List<Menu> menus = menuRepository.findAll();
        Menu menuSample = menus.stream()
                .findFirst()
                .orElseThrow();
        assertThat(menuSample.getMenu()).isEqualTo(menu);

        // when
        DeleteUserRequestDto requestDto = DeleteUserRequestDto.builder()
                .id(TestDataUtil.USER_ID)
                .email(TestDataUtil.EMAIL)
                .build();

        ResultActions action = mvc.perform(
                post(TestDataUtil.DELETE_USER_URL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andDo(print());

        assertThat(userRepository.findAll()).isEmpty();
        assertThat(businessRepository.findAll()).isEmpty();
        assertThat(tableInfoRepository.findAll()).isEmpty();
        assertThat(menuRepository.findAll()).isEmpty();
    }

}
