package com.assetManager.server.controller.updateUser.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;
    private User user;

    @Builder
    @JsonCreator
    public UpdateUserResponseDto(CommonResponseResult resultStatus, String reason, User user) {
        this.resultStatus = resultStatus;
        this.reason = reason;
        this.user = user;
    }

}
