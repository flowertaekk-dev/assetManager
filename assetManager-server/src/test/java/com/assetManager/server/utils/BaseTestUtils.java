package com.assetManager.server.utils;

import com.assetManager.server.domain.account.AccountRepository;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTestUtils {

    @Autowired private AccountRepository accountRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BusinessRepository businessRepository;
    @Autowired private TableInfoRepository tableInfoRepository;
    @Autowired private MenuRepository menuRepository;
    @Autowired private EmailAuthRepository emailAuthRepository;

    public void deleteAllDataBase() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
        businessRepository.deleteAll();
        tableInfoRepository.deleteAll();
        menuRepository.deleteAll();
        emailAuthRepository.deleteAll();
    }

}
