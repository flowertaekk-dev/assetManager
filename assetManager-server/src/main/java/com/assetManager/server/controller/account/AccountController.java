package com.assetManager.server.controller.account;

import com.assetManager.server.controller.account.dto.SaveAccountRequestDto;
import com.assetManager.server.controller.account.dto.SaveAccountResponseDto;
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
public class AccountController {
    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @PostMapping("/account/save")
    public ResponseEntity<SaveAccountResponseDto> saveAccountInfo(@RequestBody SaveAccountRequestDto request) {
        logger.info(
                String.format("saveAccountInfo -> businessId: %s, json: %s", request.getBusinessId(), request.getContents()));
        return ResponseEntity.ok(accountService.saveAccountInfo(request));
    }
}
