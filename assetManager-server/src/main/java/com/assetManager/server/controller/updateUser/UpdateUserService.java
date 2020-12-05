package com.assetManager.server.controller.updateUser;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.updateUser.dto.UpdateUserRequestDto;
import com.assetManager.server.controller.updateUser.dto.UpdateUserResponseDto;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateUserService {

    private final UserRepository userRepository;

    public UpdateUserResponseDto updatePassword(UpdateUserRequestDto requestDto) {
        Optional<User> user = userRepository.findByIdAndEmail(requestDto.getId(), requestDto.getEmail());

        if (user.isEmpty()) {
            return UpdateUserResponseDto.builder()
                    .resultStatus(CommonResponseResult.FAILURE)
                    .reason("Not found user")
                    .build();
        }

        // update password
        user.get()
                .updatePassword(requestDto.getUpdatingPassword());

        return UpdateUserResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .user(user.get())
                .build();
    }
}
