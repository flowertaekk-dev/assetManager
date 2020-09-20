package com.assetManager.server.controller.signup;

import com.assetManager.server.controller.signup.dto.SignUpRequestDto;
import com.assetManager.server.controller.signup.dto.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SignUpController {
    private static Logger logger = LoggerFactory.getLogger(SignUpController.class);

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        log(signUpRequestDto);
        return ResponseEntity.ok(signUpService.signUp(signUpRequestDto));
    }

    private void log(SignUpRequestDto signUpRequestDto) {
        logger.info(
                String.format("Received data: [id]: %s, [email]: %s", signUpRequestDto.getId(), signUpRequestDto.getEmail()));
    }
}
