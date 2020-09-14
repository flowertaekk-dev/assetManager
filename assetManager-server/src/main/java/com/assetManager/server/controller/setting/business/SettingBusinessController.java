package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.setting.business.dto.SettingBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.SettingBusinessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/business")
public class SettingBusinessController {

    private final SettingBusinessService settingBusinessService;

    @PostMapping("/add")
    public ResponseEntity<SettingBusinessResponseDto> addNewBusinessName(@RequestBody SettingBusinessRequestDto request) {
        SettingBusinessResponseDto response = settingBusinessService.addNewBusinessName(request);
        return ResponseEntity.ok(response);
    }

}
