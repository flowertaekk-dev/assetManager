package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.setting.business.dto.*;
import com.assetManager.server.domain.business.BusinessRepository;
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
@RequestMapping("/api/v1/business")
public class SettingBusinessController {
    private static Logger logger = LoggerFactory.getLogger(SettingBusinessController.class);

    private final SettingBusinessService settingBusinessService;
    private final BusinessRepository repo;

    @PostMapping("/add")
    public ResponseEntity<CommonBusinessResponseDto> addNewBusinessName(@RequestBody AddBusinessRequestDto request) {
        logger.info(String.format("addNewBusinessName: ID: %s, BusinessName: %s", request.getUserId(), request.getBusinessName()));
        CommonBusinessResponseDto response = settingBusinessService.addNewBusinessName(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<CommonBusinessResponseDto> updateBusinessName(@RequestBody UpdateBusinessRequestDto request) {
        logger.info(String.format("updateBusinessName: ID: %s, BusinessName: %s, newBusinessName: %s", request.getUserId(), request.getExistingBusinessName(), request.getNewBusinessName()));
        CommonBusinessResponseDto response = settingBusinessService.updateBusinessName(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonBusinessResponseDto> deleteBusinessName(@RequestBody DeleteBusinessRequestDto request) {
        logger.info(String.format("deleteBusinessName: ID: %s, businessName: %s", request.getUserId(), request.getBusinessName()));
        CommonBusinessResponseDto response = settingBusinessService.deleteBusinessName(request);
        return ResponseEntity.ok(response);
    }

}
