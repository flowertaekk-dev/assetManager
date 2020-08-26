package com.assetManager.server.service.login;

import com.assetManager.server.api.login.dto.LoginRequestDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LoginService {

    public static final String LOG_IN_SUCCESS = "SUCCESS";
    public static final String LOG_IN_FAILURE = "FAILURE";

    private final UserRepository userRepository;

    /***
     * 로그인에 성공하면 true, 아니면 false
     */
    public String login(LoginRequestDto loginRequestDto) {
        User loginUser = userRepository.findByIdAndPassword(
                loginRequestDto.getId(),
                loginRequestDto.getPassword())
                .orElse(null);

        return Objects.nonNull(loginUser) ? LOG_IN_SUCCESS : LOG_IN_FAILURE;
    }
}
