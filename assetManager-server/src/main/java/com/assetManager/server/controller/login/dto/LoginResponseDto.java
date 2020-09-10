package com.assetManager.server.controller.login.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public LoginResponseDto(CommonResponseResult resultStatus, String reason) {
        this.resultStatus = resultStatus;
        this.reason = reason;
    }
}
