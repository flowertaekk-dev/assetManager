package com.assetManager.server.controller.login;

import static com.assetManager.server.controller.CommonResponseResult.SUCCESS;
import static com.assetManager.server.controller.CommonResponseResult.FAILURE;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.controller.login.dto.LoginResponseDto;
import com.assetManager.server.controller.login.dto.RequestSaltDto;
import com.assetManager.server.controller.login.dto.ResponseSaltDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;

    /***
     * 로그인에 성공하면 true, 아니면 false
     */
    protected LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User loginUser = userRepository.findByIdAndPassword(
                loginRequestDto.getId(),
                loginRequestDto.getPassword())
                .orElse(null);

        if (Objects.isNull(loginUser)) {
            return LoginResponseDto.builder()
                    .resultStatus(FAILURE)
                    .reason("ID 또는 password를 확인해주세요")
                    .build();
        }

        // UserStatus가 '사용중'이 아니면 로그인 불가
        if (loginUser.getStatus() != User.UserStatus.USING) {
            return LoginResponseDto.builder()
                    .resultStatus(FAILURE)
                    .reason("탈퇴 처리 예약중인 아이디입니다.")
                    .build();
        }

        return LoginResponseDto.builder()
                .resultStatus(SUCCESS)
                .build();
    }

    protected ResponseSaltDto querySalt(RequestSaltDto requestSaltDto) {
        Optional<String> queryResult = userRepository.findSaltById(requestSaltDto.getId());

        if (queryResult.isEmpty())
            return ResponseSaltDto.builder()
                    .resultStatus(FAILURE)
                    .reason("올바른 ID를 입력했는지 확인해주세요")
                    .build();

        return ResponseSaltDto.builder()
                .resultStatus(SUCCESS)
                .salt(queryResult.get())
                .build();
    }
}
