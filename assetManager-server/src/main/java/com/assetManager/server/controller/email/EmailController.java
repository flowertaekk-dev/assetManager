package com.assetManager.server.controller.email;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.email.dto.EmailRequestDto;
import com.assetManager.server.controller.email.dto.EmailResponseDto;
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
public class EmailController {
    private static Logger logger = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;

    @PostMapping("/auth/email")
    public ResponseEntity<EmailResponseDto> emailConfirm(@RequestBody EmailRequestDto email) {
        logger.info(String.format("Email to send: %s", email));

        emailService.sendEmail(email.getAddressTo());

        return ResponseEntity.ok(
                EmailResponseDto.builder()
                        .resultStatus(CommonResponseResult.SUCCESS)
                        .build());
    }

}
