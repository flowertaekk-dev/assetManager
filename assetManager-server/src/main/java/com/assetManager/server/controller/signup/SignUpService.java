package com.assetManager.server.controller.signup;

import static com.assetManager.server.controller.CommonResponseResult.SUCCESS;
import static com.assetManager.server.controller.CommonResponseResult.FAILURE;

import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.controller.signup.dto.SignUpResponseDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;

    @Transactional
    protected SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        if (Objects.isNull(signUpRequestDto))
            return SignUpResponseDto.builder()
                    .resultStatus(FAILURE)
                    .reason("잘못된 입력이에요.")
                    .build();

        // 동일한 이메일로는 회원가입 불가!
        Optional<User> emailCheckResult = userRepository.findByEmail(signUpRequestDto.getEmail());
        if (emailCheckResult.isPresent()) {
            return SignUpResponseDto.builder()
                    .resultStatus(FAILURE)
                    .reason("이미 동일한 이메일로 가입되어 있습니다.")
                    .build();
        }

        // 올바른 인증코드를 입력 받았는지 확인
        EmailAuth emailAuth = emailAuthRepository.findByAuthCode(signUpRequestDto.getEmailAuthCode());
        if (emailAuth == null) {
            return SignUpResponseDto.builder()
                    .resultStatus(FAILURE)
                    .reason("틀린 이메일 인증코드입니다.")
                    .build();
        }

        // 여기까지 왔으면 무조건 성공?
        emailAuth.updateStatusToCompleted();
        userRepository.save(signUpRequestDto.toUserEntity());

        emailAuthRepository.flush();

        return SignUpResponseDto.builder()
                .resultStatus(SUCCESS)
                .reason(null)
                .build();
    }

}
