package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.domain.menu.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteMenuRequestDto {

    private String userId;
    private String businessName;
    private String menu;

    @Builder
    public DeleteMenuRequestDto(String userId, String businessName, String menu) {
        this.userId = userId;
        this.businessName = businessName;
        this.menu = menu;
    }
}
