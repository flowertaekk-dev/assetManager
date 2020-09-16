package com.assetManager.server.controller.setting.business.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadAllBusinessRequestDto {

    private String userId;

    @Builder
    public ReadAllBusinessRequestDto(String userId) {
        this.userId = userId;
    }
}
