package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.tableCount.TableCount;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateTableCountResponseDto {

    private TableCount tableCount;
    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public UpdateTableCountResponseDto(TableCount tableCount, CommonResponseResult resultStatus, String reason) {
        this.tableCount = tableCount;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환newBusinessName
     */
    public static UpdateTableCountResponseDto makeSuccessResponse(TableCount tableCount) {
        return UpdateTableCountResponseDto.builder()
                .tableCount(tableCount)
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static UpdateTableCountResponseDto makeFailureResponse(String reason) {
        return UpdateTableCountResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }
}
