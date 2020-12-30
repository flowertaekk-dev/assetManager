package com.assetManager.server.account;

import com.assetManager.server.controller.account.dto.SaveAccountRequestDto;
import com.assetManager.server.domain.account.Account;
import com.assetManager.server.domain.account.AccountRepository;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.utils.BaseTestUtils;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.utils.dummy.DummyCreator;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assetManager.server.utils.TestDataUtil.*;

//@ActiveProfiles(profiles = "local")
@SpringBootTest
//@Transactional
public class AccountTest extends BaseTestUtils {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private AccountRepository accountRepository;

    @Autowired private DummyCreator dummyCreator;

    private MockMvc mvc;

    @BeforeEach
    public void setup() throws Exception {
        deleteAllDataBase();
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        dummyCreator.createMenu("coffee", 2000);
    }

    @AfterEach
    public void tearDown() {}

    // case: 장부데이터의 JSON 데이터를 받아서 DB에 저장한다.
    @Test public void test_can_save_account_data() throws Exception {
        // given

        List<Business> business = businessRepository.findByUserId(USER_ID);
        assertThat(business).isNotEmpty();
        assertThat(business.size()).isEqualTo(1);

        String businessId = business.get(0).getBusinessId();

        final String jsonData =
                "{"
                        + "'" + businessId + "': {"
                        + "'businessID': '"+ businessId +"',"
                        + "'menuId': 'MU-00-00',"
                        + "'count': '2',"
                        + "'menu': '냉면',"
                        + "'price': '6000',"
                        + "'totalPrice': '12000',"
                        + "'userId': '" + TestDataUtil.USER_ID + "'"
                        + "}"
                        + "}";

        // when
        String content = objectMapper.writeValueAsString(
                SaveAccountRequestDto.builder()
                        .businessId(businessId)
                        .contents(objectMapper.writeValueAsString(jsonData))
                        .build());

        ResultActions action = mvc.perform(
                post(ACCOUNT_CONTROLLER_URL + "/save")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andExpect(jsonPath("$.account", notNullValue()))
                .andDo(print());

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotEmpty();
        assertThat(accounts.get(0).getAccountId()).isNotNull();
        assertThat(accounts.get(0).getBusinessId()).isEqualTo(businessId);
        assertThat(accounts.get(0).getContents()).isNotNull();
    }

}
