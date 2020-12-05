package com.assetManager.server.controller.updateUser;

import com.assetManager.server.controller.updateUser.dto.UpdateUserRequestDto;
import com.assetManager.server.controller.updateUser.dto.UpdateUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UpdateUserController {

    private final UpdateUserService updateUserService;

    @PostMapping("/updatePassword")
    public ResponseEntity<UpdateUserResponseDto> updatePassword(@RequestBody UpdateUserRequestDto requestDto) {
        return ResponseEntity.ok(updateUserService.updatePassword(requestDto));
    }

}
