package com.assetManager.server.controller.signup;

import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.ok(signUpService.signUp(signUpRequestDto));
    }
}
