package com.assetManager.server.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    private void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void can_save_user() {
        // given
        String id = "test";
        String password = "test";
        String email = "test@test.com";

        // when
        userRepository.save(
                User.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());

        // then
        List<User> users = userRepository.findAll();
        assertThat(users).isNotEmpty();

        User targetUser = users.get(0);
        assertThat(targetUser.getId()).isEqualTo(id);
        assertThat(targetUser.getPassword()).isEqualTo(password);
        assertThat(targetUser.getEmail()).isEqualTo(email);
    }

    @Test
    public void can_log_in_with_correct_id_and_password() {
        // given
        String id = "test";
        String password = "test";
        String email = "test@test.com";

        userRepository.save(
                User.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());

        // when
        User loginUser = userRepository.findByIdAndPassword(id, password)
                .orElse(null);

        // then
        assertThat(loginUser).isNotNull();
        assertThat(loginUser.getId()).isEqualTo(id);
        assertThat(loginUser.getPassword()).isEqualTo(password);
        assertThat(loginUser.getEmail()).isEqualTo(email);
    }

    @Test
    public void can_log_in_with_wrong_id_and_password() {
        // given
        String id = "test";
        String password = "test";
        String email = "test@test.com";

        userRepository.save(
                User.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .build());

        // when
        String wrongPassword = "wrong";

        User loginUser = userRepository.findByIdAndPassword(id, wrongPassword)
                .orElse(null);

        // then
        assertThat(loginUser).isNull();
    }

    }
