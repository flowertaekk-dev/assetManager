package com.assetManager.server.controller.setting.business.dto;

import com.assetManager.server.domain.business.Business;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettingBusinessRequestDto {

    private String userId;
    private String businessName;

    @Builder
    public SettingBusinessRequestDto(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }

    public Business toBusinessEntity() {
        return Business.builder()
                .userId(this.userId)
                .businessName(this.businessName)
                .build();
    }
}
