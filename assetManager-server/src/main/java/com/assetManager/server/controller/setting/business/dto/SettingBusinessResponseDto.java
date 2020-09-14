package com.assetManager.server.controller.setting.business.dto;

import com.assetManager.server.controller.CommonResponseResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingBusinessResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    public SettingBusinessResponseDto(CommonResponseResult resultStatus, String reason) {
        this.resultStatus = resultStatus;
        this.reason = reason;
    }
}
