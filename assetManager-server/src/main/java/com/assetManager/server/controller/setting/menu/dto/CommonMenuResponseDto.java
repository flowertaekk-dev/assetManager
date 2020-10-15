package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.menu.Menu;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonMenuResponseDto {

    private Menu menu;
    private CommonResponseResult resultStatus;
    private String reason;

    @Builder
    @JsonCreator
    public CommonMenuResponseDto(Menu menu, CommonResponseResult resultStatus, String reason) {
        this.menu = menu;
        this.resultStatus = resultStatus;
        this.reason = reason;
    }

    // ------------------------------------------------------------------------
    // utils

    /**
     * SUCCESS Response를 반환
     */
    public static CommonMenuResponseDto makeSuccessResponse(Menu menu) {
        return CommonMenuResponseDto.builder()
                .menu(menu)
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * FAILURE Response를 반환
     */
    public static CommonMenuResponseDto makeFailureResponse(String reason) {
        return CommonMenuResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }

}
