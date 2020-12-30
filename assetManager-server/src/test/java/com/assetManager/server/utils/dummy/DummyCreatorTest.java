package com.assetManager.server.utils.dummy;

import com.assetManager.server.domain.account.AccountRepository;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.domain.user.UserRepository;
import com.assetManager.server.utils.BaseTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(profiles = "local")
@SpringBootTest
public class DummyCreatorTest extends BaseTestUtils {

    @Autowired private DummyCreator dummyCreator;

    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private EmailAuthRepository emailAuthRepository;
    @Autowired private AccountRepository accountRepository;

    @BeforeEach public void setUp() { deleteAllDataBase(); }
    @AfterEach public void tearDown() {}

    @Test public void test_can_create_dummy_user() {
        checkEmpty(userRepository);
        dummyCreator.createUser();
        checkNotEmpty(userRepository);
    }

    @Test public void test_can_create_dummy_business() {
        checkEmpty(userRepository, businessRepository);
        dummyCreator.createBusiness();
        checkNotEmpty(userRepository, businessRepository);
    }

    @Test public void test_can_create_dummy_table_info() {
        checkEmpty(userRepository, businessRepository, tableInfoRepository);
        dummyCreator.createTableInfo(1);
        checkNotEmpty(userRepository, businessRepository, tableInfoRepository);
    }

    @Test public void test_can_create_dummy_menu() {
        checkEmpty(userRepository, businessRepository, menuRepository);
        dummyCreator.createMenu("testMenu", 2000);
        checkNotEmpty(userRepository, businessRepository, menuRepository);
    }

    @Test public void test_can_create_dummy_table_info_with_menu() {
        checkEmpty(userRepository, businessRepository, tableInfoRepository, menuRepository);
        dummyCreator.createTableInfoWithMenu(1, "testMenu", 2000);
        checkNotEmpty(userRepository, businessRepository, tableInfoRepository, menuRepository);
    }

    @Test public void test_can_create_dummy_emailAuth() {
        // emailAuth는 prefix 없이 long 타입으로 id 생성중
        assertThat(emailAuthRepository.findAll()).isEmpty();
        dummyCreator.createEmailAuth(EmailAuth.EmailAuthStatus.SENT);
        assertThat(emailAuthRepository.findAll()).isNotEmpty();
    }

    @Test public void test_can_create_dummy_account() {
        checkEmpty(accountRepository);
        dummyCreator.createAccount("testMenu", 6000);
        checkNotEmpty(accountRepository);
    }

    // ------------------------------------------------------------

    private void checkEmpty(JpaRepository<?, String> ...repositories) {
        Arrays.stream(repositories)
                .forEach(repo -> assertThat(repo.findAll()).isEmpty());
    }

    private void checkNotEmpty(JpaRepository<?, String> ...repositories) {
        Arrays.stream(repositories)
                .forEach(repo -> assertThat(repo.findAll()).isNotEmpty());
    }

}
