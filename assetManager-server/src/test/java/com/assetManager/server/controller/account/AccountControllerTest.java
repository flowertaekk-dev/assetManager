package com.assetManager.server.controller.account;

import static com.assetManager.server.utils.TestDataUtil.ACCOUNT_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import com.assetManager.server.controller.account.dto.SaveAccountRequestDto;
import com.assetManager.server.controller.setting.business.BusinessTestUtil;
import com.assetManager.server.controller.setting.table.TableTestUtil;
import com.assetManager.server.controller.signup.UserTestUtil;
import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.domain.account.Account;
import com.assetManager.server.domain.account.AccountRepository;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
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
public class AccountControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private AccountRepository accountRepository;

    private MockMvc mvc;
    private final String BUSINESS_NAME = "testBusiness";
    private final int TABLE_COUNT = 2;

    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        UserTestUtil.insertUser();
        BusinessTestUtil.insertBusinessName(mvc, this.BUSINESS_NAME);
        String businessId = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, this.BUSINESS_NAME)
                .orElseThrow()
                .getBusinessId();

        TableTestUtil.insertTableInfo(mvc, businessId, this.TABLE_COUNT)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultStatus", is("SUCCESS")))
                .andExpect(jsonPath("$.reason", nullValue()))
                .andDo(print());
    }

    @AfterEach
    public void clean() {
        accountRepository.deleteAll();
        tableInfoRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
    }

    // case: 장부데이터의 JSON 데이터를 받아서 DB에 저장한다.
    @Test
    public void test_can_save_account_data() throws Exception {
        // given
        String businessId = businessRepository.findByUserIdAndBusinessName(TestDataUtil.USER_ID, this.BUSINESS_NAME)
                .orElseThrow()
                .getBusinessId();

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
        assertThat(accounts.get(0).getContents()).isNotNull(); // TODO JSON데이터를 확인할 수는 없나?
    }

}
