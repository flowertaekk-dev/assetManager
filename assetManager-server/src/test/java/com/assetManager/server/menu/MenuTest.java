package com.assetManager.server.menu;

import com.assetManager.server.controller.setting.menu.dto.AddMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.DeleteMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.ReadAllMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.UpdateMenuRequestDto;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;


@ActiveProfiles(profiles = "local")
@SpringBootTest
public class MenuTest extends BaseTestUtils {

    @Autowired private BusinessRepository businessRepository;
    @Autowired private MenuRepository menuRepository;

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;
    private final String MENU_NAME = "coffee";
    private final int PRICE = 2000;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void tearDown() { deleteAllDataBase(); }

    @Test public void can_save_menu() throws Exception {
        // given
        dummyCreator.createBusiness();

        List<Business> business = businessRepository.findByUserId(USER_ID);
        assertThat(business).as("더미 비지니스 데이터를 생성 실패").isNotEmpty();

        // when
        String content = objectMapper.writeValueAsString(
                AddMenuRequestDto.builder()
                        .userId(USER_ID)
                        .businessId(business.get(0).getBusinessId())
                        .menu("coffee")
                        .price(2000)
                        .build());

        ResultActions action = mvc.perform(
                post(MENU_CONTROLLER_URL + "/add")
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

    // case: 동일한 메뉴가 존재하면 실패한다.
    @Test public void cannot_save_menu_when_already_exist() throws Exception {
        // given

        Menu menu = dummyCreator.createMenu(MENU_NAME, PRICE);

        // when
        // 동일한 메뉴 등록 시도
        String content = objectMapper.writeValueAsString(
                AddMenuRequestDto.builder()
                        .userId(USER_ID)
                        .businessId(menu.getBusinessId())
                        .menu(MENU_NAME)
                        .price(PRICE)
                        .build());

        ResultActions action = mvc.perform(
                post(MENU_CONTROLLER_URL + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: 메뉴를 변경한다.
    @Test public void test_can_change_menu() throws Exception {
        // given
        String newMenu = "newCoffee";

        Menu menu = dummyCreator.createMenu(MENU_NAME, PRICE);

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(menu.getBusinessId())
                        .existingMenu(MENU_NAME)
                        .newMenu(newMenu)
                        .price(PRICE)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.MENU_CONTROLLER_URL + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).isNotEmpty();
        assertThat(menus.get(0).getUserId()).isEqualTo(TestDataUtil.USER_ID);
        assertThat(menus.get(0).getBusinessId()).isEqualTo(menu.getBusinessId());
        assertThat(menus.get(0).getMenu()).isEqualTo(newMenu);
        assertThat(menus.get(0).getPrice()).isEqualTo(PRICE);
    }

    // case: 메뉴 가격을 변경한다.
    @Test public void test_can_change_price() throws Exception {
        // given
        int newPrice = 3000;

        Menu menu = dummyCreator.createMenu(MENU_NAME, PRICE);

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(menu.getBusinessId())
                        .existingMenu(menu.getMenu())
                        .price(newPrice)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.MENU_CONTROLLER_URL + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then

        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        List<Menu> menus = menuRepository.findAll();
        assertThat(menus).isNotEmpty();
        assertThat(menus.get(0).getUserId()).isEqualTo(TestDataUtil.USER_ID);
        assertThat(menus.get(0).getBusinessId()).isEqualTo(menu.getBusinessId());
        assertThat(menus.get(0).getMenu()).isEqualTo(MENU_NAME);
        assertThat(menus.get(0).getPrice()).isEqualTo(newPrice);
    }

    // case: 변경 대상 메뉴가 존재하지 않으면 FAILURE 를 반환한다.
    @Test public void test_fail_when_cannot_find_target_menu() throws Exception {
        // given
        // do nothing

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId("fakeBusinessId")
                        .existingMenu("fakeMenuName")
                        .price(2000)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.MENU_CONTROLLER_URL + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", notNullValue()))
                .andDo(print());
    }

    // case: 메뉴를 삭제한다.
    @Test public void test_can_delete_menu() throws Exception {
        // given

        Menu menu = dummyCreator.createMenu(MENU_NAME, PRICE);

        // when
        String content = objectMapper.writeValueAsString(
                DeleteMenuRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(menu.getBusinessId())
                        .menu(menu.getMenu())
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.MENU_CONTROLLER_URL + "/delete")
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

    // case: 유저 아이디와 상호명에 연결된 메뉴를 전부 불러올 수 있다.
    @Test public void test_can_retrieve_menus_by_userId_and_businessId() throws Exception {
        // given
        String sndMenuName = "sndCoffee";

        // 첫 번째 메뉴
        Menu menu1 = dummyCreator.createMenu(MENU_NAME, PRICE);

        // 두 번째 메뉴
        Menu menu2 = sndMenuSupplier.get(menu1.getBusinessId(), sndMenuName);

        // when
        String content = objectMapper.writeValueAsString(
                ReadAllMenuRequestDto.builder()
                        .userId(TestDataUtil.USER_ID)
                        .businessId(menu1.getBusinessId())
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.MENU_CONTROLLER_URL + "/readAll")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andExpect(jsonPath("$.menus[0].menu", is(menu1.getMenu())))
                .andExpect(jsonPath("$.menus[1].menu", is(menu2.getMenu())))
                .andDo(print());

    }

    // ----------------------------------------------------------------------------

    private SndMenuSupplier<String, String, Menu> sndMenuSupplier = (businessId, sndMenuName) ->
            menuRepository.save(
                    Menu.builder()
                        .menuId(RandomIdCreator.createMenuId())
                        .userId(USER_ID)
                        .businessId(businessId)
                        .menu(sndMenuName)
                        .price(2000)
                        .build());

    @FunctionalInterface
    private interface SndMenuSupplier<A, B, R> {
        R get(A a, B b);
    }

}
