package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.UpdateTableCountRequestDto;
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

    public static ResultActions insertTableInfo(MockMvc mvc, String businessId, int tableCount) throws Exception {
        String content = staticObjectMapper.writeValueAsString(
                UpdateTableCountRequestDto.builder()
                        .userId(TestDataUtil.id)
                        .businessId(businessId)
                        .tableCount(tableCount)
                        .build());

        return mvc.perform(
                post(TestDataUtil.tableInfoControllerUrl + "/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

}
