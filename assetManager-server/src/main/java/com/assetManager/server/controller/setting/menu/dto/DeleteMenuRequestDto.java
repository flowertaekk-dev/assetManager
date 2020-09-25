package com.assetManager.server.controller.setting.menu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteMenuRequestDto {

    private String userId;
    private String businessId;
    private String menu;

    @Builder
    public DeleteMenuRequestDto(String userId, String businessId, String menu) {
        this.userId = userId;
        this.businessId = businessId;
        this.menu = menu;
    }
}
