package com.assetManager.server.controller.user.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUserResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public DeleteUserResponseDto(CommonResponseResult resultStatus, String reason) {
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

}
