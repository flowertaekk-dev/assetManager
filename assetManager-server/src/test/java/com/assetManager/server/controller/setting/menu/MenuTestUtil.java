package com.assetManager.server.controller.setting.menu;

import com.assetManager.server.controller.setting.menu.dto.AddMenuRequestDto;
import com.assetManager.server.controller.utils.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class MenuTestUtil {

    @Autowired private ObjectMapper objectMapper;
    private static ObjectMapper staticObjectMapper;
    @PostConstruct private void initObjectMapper() {
        staticObjectMapper = this.objectMapper;
    }

    public static ResultActions insertTableCount(MockMvc mvc, String businessId, String menu, int price) throws Exception {
        String content = staticObjectMapper.writeValueAsString(
                AddMenuRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessId(businessId)
                        .menu(menu)
                        .price(price)
                        .build());

        return mvc.perform(
                post(TestDataUtil.menuControllerUrl + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }
}
