package com.assetManager.server.controller.login;

import static com.assetManager.server.controller.CommonResponseResult.SUCCESS;
import static com.assetManager.server.controller.CommonResponseResult.FAILURE;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;

    /***
     * 로그인에 성공하면 true, 아니면 false
     */
    protected String login(LoginRequestDto loginRequestDto) {
        User loginUser = userRepository.findByIdAndPassword(
                loginRequestDto.getId(),
                loginRequestDto.getPassword())
                .orElse(null);

        if (Objects.isNull(loginUser)) {
            return FAILURE.name();
        }

        // UserStatus가 '사용중'이 아니면 로그인 불가
        if (loginUser.getStatus() != User.UserStatus.USING) {
            return FAILURE.name();
        }

        return SUCCESS.name();
    }
}
