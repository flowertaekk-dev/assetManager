package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.UpsertTableCountRequestDto;
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
public class TableTestUtil extends TestDataUtil {

    @Autowired private ObjectMapper objectMapper;
    private static ObjectMapper staticObjectMapper;
    @PostConstruct private void initObjectMapper() {
        staticObjectMapper = this.objectMapper;
    }

    public static ResultActions insertTableCount(MockMvc mvc, String businessName, int tableCount) throws Exception {
        String content = staticObjectMapper.writeValueAsString(
                UpsertTableCountRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessName(businessName)
                        .tableCount(tableCount)
                        .build());

        return mvc.perform(
                post(TestDataUtil.tableCountControllerUrl + "/upsert")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

}
