package com.assetManager.server.controller.user;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.user.dto.DeleteUserRequestDto;
import com.assetManager.server.controller.user.dto.DeleteUserResponseDto;
import com.assetManager.server.controller.user.dto.UpdateUserRequestDto;
import com.assetManager.server.controller.user.dto.UpdateUserResponseDto;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import com.assetManager.server.domain.user.User;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final TableInfoRepository tableInfoRepository;
    private final MenuRepository menuRepository;

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

    public DeleteUserResponseDto deleteUser(DeleteUserRequestDto requestDto) {
        // menu
        menuRepository.deleteByUserId(requestDto.getId());

        // table
        tableInfoRepository.deleteByUserId(requestDto.getId());

        // business
        businessRepository.deleteByUserId(requestDto.getId());

        // user
        userRepository.deleteById(requestDto.getId());

        return DeleteUserResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }
}
