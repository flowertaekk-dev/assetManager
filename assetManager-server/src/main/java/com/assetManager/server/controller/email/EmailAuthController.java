package com.assetManager.server.controller.email;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.email.dto.EmailAuthRequestDto;
import com.assetManager.server.controller.email.dto.EmailAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class EmailAuthController {
    private static Logger logger = LoggerFactory.getLogger(EmailAuthController.class);

    private final EmailAuthService emailAuthService;

    @PostMapping("/email/requestCode")
    public ResponseEntity<EmailAuthResponseDto> emailConfirm(@RequestBody EmailAuthRequestDto email) {
        logger.info(String.format("Email to send: %s", email));

        EmailAuthResponseDto responseDto = emailAuthService.sendEmail(email.getAddressTo());
        return ResponseEntity.ok(responseDto);
    }

}
