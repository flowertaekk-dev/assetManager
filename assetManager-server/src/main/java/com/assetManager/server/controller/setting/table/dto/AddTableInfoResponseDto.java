package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddTableInfoResponseDto {

    private TableInfo tableInfo;
    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public AddTableInfoResponseDto(TableInfo tableInfo, CommonResponseResult resultStatus, String reason) {
        this.tableInfo = tableInfo;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환
     */
    public static AddTableInfoResponseDto makeSuccessResponse(TableInfo tableInfo) {
        return AddTableInfoResponseDto.builder()
                .tableInfo(tableInfo)
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static AddTableInfoResponseDto makeFailureResponse(String reason) {
        return AddTableInfoResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }
}
