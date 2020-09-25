package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.tableInfo.TableInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadTableCountRequestDto {

    private String userId;
    private String businessId;

    @Builder
    public ReadTableCountRequestDto(String userId, String businessId) {
        this.userId = userId;
        this.businessId = businessId;
    }

    public TableInfo toTableCountEntity() {
        return TableInfo.builder()
                .userId(this.userId)
                .businessId(this.businessId)
                .build();
    }
}
