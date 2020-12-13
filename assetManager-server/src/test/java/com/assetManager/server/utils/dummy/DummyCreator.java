package com.assetManager.server.utils.dummy;

import com.assetManager.server.domain.account.Account;
import com.assetManager.server.domain.account.AccountRepository;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import com.assetManager.server.utils.RandomIdCreator;
import com.assetManager.server.utils.TestDataUtil;
import net.bytebuddy.utility.RandomString;
import org.assertj.core.util.TriFunction;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
public class DummyCreator extends TestDataUtil {

    @Autowired private AccountRepository accountRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private EmailAuthRepository emailAuthRepository;

    // -------------------------------------------------------------------

    public User createUser() {
        return userRepository.save(
                User.builder()
                        .id(USER_ID)
                        .salt(SALT)
                        .password(PASSWORD)
                        .email(EMAIL)
                        .status(User.UserStatus.USING)
                        .build());
    }

    public Business createBusiness() {
        var user = createUser();
        return businessRepository.save(
                Business.builder()
                        .businessId(RandomIdCreator.createBusinessId())
                        .userId(user.getId())
                        .businessName(BUSINESS)
                        .build());
    }

    public TableInfo createTableInfo(int tableCount) {
        var business = createBusiness();
        return tableInfoRepository.save(
                TableInfo.builder()
                        .tableInfoId(RandomIdCreator.createTableInfoId())
                        .userId(business.getUserId())
                        .businessId(business.getBusinessId())
                        .tableCount(tableCount)
                        .build());
    }

    public Menu createMenu(String menu, int price) {
        var business = createBusiness();
        return menuCreator.apply(business, menu, price);
    }

    public void createTableInfoWithMenu(int tableCount, String menu, int price) {
        var business = createBusiness();
        createTableInfo(1);
        menuCreator.apply(business, menu, price);
    }

    public EmailAuth createEmailAuth(EmailAuth.EmailAuthStatus status) {
        return emailAuthRepository.save(
                EmailAuth.builder()
                        .email(EMAIL)
                        .authCode(RandomString.make())
                        .status(EmailAuth.EmailAuthStatus.SENT)
                        .build());
    }

    public Account createAccount(String menuName, int price) {
        var menu = createMenu(menuName, price);

        final String jsonData =
                "{"
                    + "'" + menu.getBusinessId() + "': {"
                        + "'businessID': '"+ menu.getBusinessId() +"',"
                        + "'menuId': 'MU-00-00',"
                        + "'count': '1',"
                        + "'menu': '" + menuName + "'  ,"
                        + "'price': '" + price + "',"
                        + "'totalPrice': '" + price + "',"
                        + "'userId': '" + TestDataUtil.USER_ID + "'"
                    + "}"
                + "}";

        return accountRepository.save(
                Account.builder()
                        .accountId(RandomIdCreator.createAccountId())
                        .businessId(menu.getBusinessId())
                        .contents(jsonData)
                        .build());
    }

    // -------------------------------------------------------------------

    private TriFunction<Business, String, Integer, Menu> menuCreator = (business, menu, price) -> {
        return menuRepository.save(
                Menu.builder()
                        .menuId(RandomIdCreator.createMenuId())
                        .userId(business.getUserId())
                        .businessId(business.getBusinessId())
                        .menu(menu)
                        .price(price)
                        .build());
    };

}
