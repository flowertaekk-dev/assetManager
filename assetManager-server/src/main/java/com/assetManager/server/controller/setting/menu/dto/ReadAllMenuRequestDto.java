package com.assetManager.server.controller.setting.menu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadAllMenuRequestDto {

    private String userId;
    private String businessId;

    @Builder
    public ReadAllMenuRequestDto(String userId, String businessId) {
        this.userId = userId;
        this.businessId = businessId;
    }
}
