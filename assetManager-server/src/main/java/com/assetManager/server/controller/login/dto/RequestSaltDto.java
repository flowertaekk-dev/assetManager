package com.assetManager.server.controller.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestSaltDto {

    private String id;

    @Builder
    public RequestSaltDto(String id) {
        this.id = id;
    }
}
