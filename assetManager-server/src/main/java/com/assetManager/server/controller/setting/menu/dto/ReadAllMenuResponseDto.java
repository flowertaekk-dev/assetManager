package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.domain.menu.Menu;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReadAllMenuResponseDto {

    private CommonResponseResult resultStatus;
    private String reason;
    private List<Menu> menus;

    @Builder
    @JsonCreator
    public ReadAllMenuResponseDto(CommonResponseResult resultStatus, String reason, List<Menu> menus) {
        this.resultStatus = resultStatus;
        this.reason = reason;
        this.menus = menus;
    }

}
