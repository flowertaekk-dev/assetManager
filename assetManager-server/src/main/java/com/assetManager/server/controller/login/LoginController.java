package com.assetManager.server.controller.login;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.controller.login.dto.LoginResponseDto;
import com.assetManager.server.controller.login.dto.RequestSaltDto;
import com.assetManager.server.controller.login.dto.ResponseSaltDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(loginService.login(loginRequestDto));
    }

    @PostMapping("/requestSalt")
    public ResponseEntity<ResponseSaltDto> querySalt(@RequestBody RequestSaltDto requestSaltDto) {
        return ResponseEntity.ok(loginService.querySalt(requestSaltDto));
    }

}
