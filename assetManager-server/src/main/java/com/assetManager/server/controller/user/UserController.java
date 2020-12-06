package com.assetManager.server.controller.user;

import com.assetManager.server.controller.user.dto.DeleteUserRequestDto;
import com.assetManager.server.controller.user.dto.DeleteUserResponseDto;
import com.assetManager.server.controller.user.dto.UpdateUserRequestDto;
import com.assetManager.server.controller.user.dto.UpdateUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/updatePassword")
    public ResponseEntity<UpdateUserResponseDto> updatePassword(@RequestBody UpdateUserRequestDto requestDto) {
        return ResponseEntity.ok(userService.updatePassword(requestDto));
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<DeleteUserResponseDto> deleteUser(@RequestBody DeleteUserRequestDto requestDto) {
        return ResponseEntity.ok(userService.deleteUser(requestDto));
    }


}
