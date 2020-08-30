package com.assetManager.server.controller.signup;

import static com.assetManager.server.controller.CommonResponseResult.SUCCESS;
import static com.assetManager.server.controller.CommonResponseResult.FAILURE;

import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;

    protected String signUp(SignUpRequestDto signUpRequestDto) {

        if (Objects.isNull(signUpRequestDto))
            return FAILURE.name();

        // TODO 이미 사용중인 ID, EMAIL인지 확인하는 로직 필요!

        // 여기까지 왔으면 무조건 성공?
        userRepository.save(signUpRequestDto.toUserEntity());
        return SUCCESS.name();
    }

}
