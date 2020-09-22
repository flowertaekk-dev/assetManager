package com.assetManager.server.controller.setting.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import com.assetManager.server.controller.setting.business.BusinessTestUtil;
import com.assetManager.server.controller.setting.menu.dto.DeleteMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.ReadAllMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.UpdateMenuRequestDto;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.controller.utils.TestDataUtil;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
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
public class MenuControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private MenuRepository menuRepository;

    private MockMvc mvc;

    private final String businessName = "assetManager";
    private final String menu = "삼겹살";
    private final int price = 2500;

    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        // 유저 데이터 초기화
        UserTestUtil.insertUser();
        BusinessTestUtil.insertBusinessName(this.mvc, this.businessName);
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
        businessRepository.deleteAll();
        menuRepository.deleteAll();
    }

    // case: 메뉴를 등록한다.
    @Test
    public void can_save_menu() throws Exception {
        // given

        // when
        ResultActions action = MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    // case: 동일한 메뉴가 존재하면 실패한다.
    @Test
    public void cannot_save_menu_when_already_exist() throws Exception {
        // given
        MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // when

        // 동일한 메뉴 등록 시도
        ResultActions action = MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price);

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", is("이미 존재하는 메뉴입니다.")))
                .andDo(print());
    }

    // case: 메뉴를 변경한다.
    @Test
    public void test_can_change_menu() throws Exception {
        // given
        String newMenu = "족발";

        MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .menu(this.menu)
                        .newMenu(newMenu)
                        .price(price)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/update")
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
        assertThat(menus.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(menus.get(0).getBusinessName()).isEqualTo(this.businessName);
        assertThat(menus.get(0).getMenu()).isEqualTo(newMenu);
        assertThat(menus.get(0).getPrice()).isEqualTo(price);
    }

    // case: 메뉴 가격을 변경한다.
    @Test
    public void test_can_change_price() throws Exception {
        // given
        int price = 5000;

        MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .menu(this.menu)
                        .price(price)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/update")
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
        assertThat(menus.get(0).getUserId()).isEqualTo(TestDataUtil.id);
        assertThat(menus.get(0).getBusinessName()).isEqualTo(this.businessName);
        assertThat(menus.get(0).getMenu()).isEqualTo(this.menu);
        assertThat(menus.get(0).getPrice()).isEqualTo(price);
    }

    // case: 변경 대상 메뉴가 존재하지 않으면 FAILURE를 반환한다.
    @Test
    public void test_fail_when_cannot_find_target_menu() throws Exception {
        // given
        // do nothing

        // when
        String content = objectMapper.writeValueAsString(
                UpdateMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .menu(this.menu)
                        .price(price)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/update")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("FAILURE")))
                .andExpect(jsonPath("$.reason", is("변경할 대상 데이터가 존재하지 않습니다.")))
                .andDo(print());
    }

    // case: 메뉴를 삭제한다.
    @Test
    public void test_can_delete_menu() throws Exception {
        // given
        MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // when
        String content = objectMapper.writeValueAsString(
                DeleteMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .menu(this.menu)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/delete")
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
    @Test
    public void test_can_retrieve_menus_by_userId_and_businessName() throws Exception {
        // given

        // 메뉴 1
        MenuTestUtil.insertTableCount(this.mvc, this.businessName, this.menu, this.price)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // 메뉴 2
        String menu2 = "냉면";
        int price2 = 5000;

        MenuTestUtil.insertTableCount(this.mvc, this.businessName, menu2, price2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());

        // when

        String content = objectMapper.writeValueAsString(
                ReadAllMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(this.businessName)
                        .build());

        ResultActions action = mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/readAll")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andExpect(jsonPath("$.menus[0].menu", is(this.menu)))
                .andExpect(jsonPath("$.menus[1].menu", is(menu2)))
                .andDo(print());

    }


}
