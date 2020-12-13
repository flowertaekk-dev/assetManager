package com.assetManager.server.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(profiles = "dev")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    private void clean() {
        userRepository.deleteAll();
    }

    private String id = "test";
    private String salt = "salt";
    private String password = "test";
    private String email = "test@test.com";

    @Test public void can_save_user() {
        // given

        // when
        userRepository.save(
                User.builder()
                        .id(id)
                        .salt(salt)
                        .password(password)
                        .email(email)
                        .status(User.UserStatus.USING)
                        .build());

        // then
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();

        User targetUser = users.get(0);
        assertThat(targetUser.getId()).isEqualTo(id);
        assertThat(targetUser.getSalt()).isEqualTo(salt);
        assertThat(targetUser.getPassword()).isEqualTo(password);
        assertThat(targetUser.getEmail()).isEqualTo(email);
    }

    @Test public void can_log_in_with_correct_id_and_password() {
        // given
        userRepository.save(
                User.builder()
                        .id(id)
                        .salt(salt)
                        .password(password)
                        .email(email)
                        .status(User.UserStatus.USING)
                        .build());

        // when
        User loginUser = userRepository.findByIdAndPassword(id, password)
                .orElse(null);

        // then
        assertThat(loginUser).isNotNull();
        assertThat(loginUser.getId()).isEqualTo(id);
        assertThat(loginUser.getSalt()).isEqualTo(salt);
        assertThat(loginUser.getPassword()).isEqualTo(password);
        assertThat(loginUser.getEmail()).isEqualTo(email);
    }

    @Test public void can_log_in_with_wrong_id_and_password() {
        // given
        userRepository.save(
                User.builder()
                        .id(id)
                        .salt(salt)
                        .password(password)
                        .email(email)
                        .status(User.UserStatus.USING)
                        .build());

        // when
        String wrongPassword = "wrong";

        User loginUser = userRepository.findByIdAndPassword(id, wrongPassword)
                .orElse(null);

        // then
        assertThat(loginUser).isNull();
    }

}
