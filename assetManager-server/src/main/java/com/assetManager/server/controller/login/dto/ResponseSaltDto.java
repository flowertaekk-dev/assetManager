package com.assetManager.server.controller.login.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseSaltDto {

    private CommonResponseResult resultStatus;
    private String salt;
    private String reason;

    @Builder
    @JsonCreator
    public ResponseSaltDto(CommonResponseResult resultStatus, String salt, String reason) {
        this.resultStatus = resultStatus;
        this.salt = salt;
        this.reason = reason;
    }
}
