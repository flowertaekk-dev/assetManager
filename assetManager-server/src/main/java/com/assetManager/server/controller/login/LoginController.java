package com.assetManager.server.controller.login;

import com.assetManager.server.controller.login.dto.LoginRequestDto;
import com.assetManager.server.service.login.LoginService;
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
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(loginService.login(loginRequestDto));
    }

}
