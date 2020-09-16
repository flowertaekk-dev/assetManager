package com.assetManager.server.controller.setting.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBusinessRequestDto {

    private String userId;
    private String existingBusinessName;
    private String newBusinessName;

    @Builder
    public UpdateBusinessRequestDto(String userId, String existingBusinessName, String newBusinessName) {
        this.userId = userId;
        this.existingBusinessName = existingBusinessName;
        this.newBusinessName = newBusinessName;
    }

}
