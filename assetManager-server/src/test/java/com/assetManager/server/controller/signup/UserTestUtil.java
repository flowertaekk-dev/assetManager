package com.assetManager.server.controller.signup;

import com.assetManager.server.utils.TestDataUtil;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserTestUtil extends TestDataUtil {

    @Autowired private UserRepository userRepository;
    private static UserRepository staticUserRepository;
    @PostConstruct private void initUserRepository() {
        staticUserRepository = this.userRepository;
    }

    /**
     * USING 상태의 유저를 생성한다
     */
    public static void insertUser() {
        staticUserRepository.save(
                User.builder()
                        .id(USER_ID)
                        .salt(SALT)
                        .password(PASSWORD)
                        .email(EMAIL)
                        .status(User.UserStatus.USING)
                        .build());
    }
}
