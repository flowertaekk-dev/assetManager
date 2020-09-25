package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.tableCount.TableCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTableCountRequestDto {

    private String userId;
    private String businessId;
    private int tableCount;

    @Builder
    public UpdateTableCountRequestDto(String userId, String businessId, int tableCount) {
        this.userId = userId;
        this.businessId = businessId;
        this.tableCount = tableCount;
    }

    public TableCount toTableCountEntity() {
        return TableCount.builder()
                .userId(this.userId)
                .businessId(this.businessId)
                .tableCount(this.tableCount)
                .build();
    }
}
