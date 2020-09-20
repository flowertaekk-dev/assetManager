package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.setting.business.dto.AddBusinessRequestDto;
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
public class BusinessTestUtil extends TestDataUtil {

    @Autowired private ObjectMapper objectMapper;
    private static ObjectMapper staticObjectMapper;
    @PostConstruct private void initObjectMapper() {
        staticObjectMapper = this.objectMapper;
    }

    /**
     * 상호명을 생성한다
     */
    public static ResultActions insertBusinessName(MockMvc mvc, String businessName) throws Exception {
        String content = staticObjectMapper.writeValueAsString(
                AddBusinessRequestDto.builder()
                        .userId(id)
                        .businessName(businessName)
                        .build());

        return mvc.perform(
                post(businessControllerUrl + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }
}
