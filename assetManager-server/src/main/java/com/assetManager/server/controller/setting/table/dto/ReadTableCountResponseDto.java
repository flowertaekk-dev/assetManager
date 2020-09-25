package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.tableCount.TableCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadTableCountResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;
    private String userId;
    private String businessId;
    private int tableCount;

    @Builder
    public ReadTableCountResponseDto(CommonResponseResult resultStatus, String reason, String userId, String businessId, int tableCount) {
        this.resultStatus = resultStatus;
        this.reason = reason;
        this.userId = userId;
        this.businessId = businessId;
        this.tableCount = tableCount;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환
     */
    public static ReadTableCountResponseDto makeSuccessResponse(TableCount entity) {
        return ReadTableCountResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .userId(entity.getUserId())
                .businessId(entity.getBusinessId())
                .tableCount(entity.getTableCount())
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static ReadTableCountResponseDto makeFailureResponse(String reason) {
        return ReadTableCountResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }

}
