package com.assetManager.server.controller.account.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.account.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveAccountResponseDto {

    private Account account;
    private CommonResponseResult resultStatus;
    private String reason;

    @JsonCreator
    @Builder
    public SaveAccountResponseDto(Account account, CommonResponseResult resultStatus, String reason) {
        this.account = account;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }
}
