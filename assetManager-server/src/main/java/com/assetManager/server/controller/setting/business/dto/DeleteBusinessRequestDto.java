package com.assetManager.server.controller.setting.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteBusinessRequestDto {

    private String userId;
    private String businessName;

    @Builder
    public DeleteBusinessRequestDto(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }
}
