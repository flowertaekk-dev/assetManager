package com.assetManager.server.user;

import com.assetManager.server.controller.user.dto.DeleteUserRequestDto;
import com.assetManager.server.controller.user.dto.UpdateUserRequestDto;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.assetManager.server.utils.BaseTestUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "local")
@SpringBootTest
@Transactional
public class UserUpdateDataTest extends BaseTestUtils {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserRepository userRepository;

    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;

    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;

    @BeforeEach public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        dummyCreator.createUser();
    }

    @AfterEach public void tearDown() {
        deleteAllDataBase();
    }

    /**
     * Can update password
     *
     * @throws Exception
     */
    @Test public void test_can_update_password() throws Exception {
        // given
        var newPassword = "newPassword";
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

    /**
     * Can delete user data as well as all related data
     *
     * @throws Exception
     */
    @Test public void test_can_delete_user_data_with_related_data() throws Exception {
        // given
        String menu = "testMenu";
        int price = 2000;

        dummyCreator.createMenu(menu, price);

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
