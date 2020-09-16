package com.assetManager.server.controller.setting.business.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.business.Business;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReadAllBusinessResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;
    private List<Business> businessNames;

    @Builder
    public ReadAllBusinessResponseDto(CommonResponseResult resultStatus, String reason, List<Business> businessNames) {
        this.resultStatus = resultStatus;
        this.reason = reason;
        this.businessNames = businessNames;
    }
}
