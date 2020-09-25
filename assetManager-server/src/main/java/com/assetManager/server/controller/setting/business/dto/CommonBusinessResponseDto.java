package com.assetManager.server.controller.setting.business.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.business.Business;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonBusinessResponseDto {

    private Business business;
    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public CommonBusinessResponseDto(Business business, CommonResponseResult resultStatus, String reason) {
        this.business = business;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환
     */
    public static CommonBusinessResponseDto makeSuccessResponse(Business business) {
        return CommonBusinessResponseDto.builder()
                .business(business)
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static CommonBusinessResponseDto makeFailureResponse(String reason) {
        return CommonBusinessResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }
}
