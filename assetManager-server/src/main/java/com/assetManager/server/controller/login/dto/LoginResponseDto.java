package com.assetManager.server.controller.login.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private CommonResponseResult resultStatus;
    private User user;
    private String reason;

    @Builder
    @JsonCreator
    public LoginResponseDto(CommonResponseResult resultStatus, User user, String reason) {
        this.resultStatus = resultStatus;
        this.user = user;
        this.reason = reason;
    }

}
