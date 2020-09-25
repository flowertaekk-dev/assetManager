package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateTableCountResponseDto {

    private TableInfo tableInfo;
    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public UpdateTableCountResponseDto(TableInfo tableInfo, CommonResponseResult resultStatus, String reason) {
        this.tableInfo = tableInfo;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환newBusinessName
     */
    public static UpdateTableCountResponseDto makeSuccessResponse(TableInfo tableInfo) {
        return UpdateTableCountResponseDto.builder()
                .tableInfo(tableInfo)
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
