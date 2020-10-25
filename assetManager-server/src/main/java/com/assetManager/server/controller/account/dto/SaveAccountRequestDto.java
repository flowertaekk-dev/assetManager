package com.assetManager.server.controller.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveAccountRequestDto {

    private String businessId;
    private String contents;

    @Builder
    public SaveAccountRequestDto(String businessId, String contents) {
        this.businessId = businessId;
        this.contents = contents;
    }
}
