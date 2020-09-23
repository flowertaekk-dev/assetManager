package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.tableCount.TableCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTableCountRequestDto {

    private String userId;
    private String businessName;
    private int tableCount;

    @Builder
    public UpdateTableCountRequestDto(String userId, String businessName, int tableCount) {
        this.userId = userId;
        this.businessName = businessName;
        this.tableCount = tableCount;
    }

    public TableCount toTableCountEntity() {
        return TableCount.builder()
                .userId(this.userId)
                .businessName(this.businessName)
                .tableCount(this.tableCount)
                .build();
    }
}
