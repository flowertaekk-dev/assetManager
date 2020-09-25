package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTableInfoRequestDto {

    private final String identityCode = "TC-";

    private String userId;
    private String businessId;
    private int tableCount;

    @Builder
    public AddTableInfoRequestDto(String userId, String businessId, int tableCount) {
        this.userId = userId;
        this.businessId = businessId;
        this.tableCount = tableCount;
    }

    public TableInfo toTableInfoEntity() {
        return TableInfo.builder()
                .tableInfoId(identityCode + RandomIdCreator.create())
                .userId(this.userId)
                .businessId(this.businessId)
                .tableCount(this.tableCount)
                .build();
    }
}
