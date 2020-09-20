package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpsertTableCountResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public UpsertTableCountResponseDto(CommonResponseResult resultStatus, String reason) {
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환
     */
    public static UpsertTableCountResponseDto makeSuccessResponse() {
        return UpsertTableCountResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static UpsertTableCountResponseDto makeFailureResponse(String reason) {
        return UpsertTableCountResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }
}
