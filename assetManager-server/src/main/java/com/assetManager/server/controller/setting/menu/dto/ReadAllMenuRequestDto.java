package com.assetManager.server.controller.setting.menu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadAllMenuRequestDto {

    private String userId;
    private String businessName;

    @Builder
    public ReadAllMenuRequestDto(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }
}
