package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.tableCount.TableCount;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddTableCountRequestDto {

    private final String identityCode = "TC-";

    private String userId;
    private String businessName;
    private int tableCount;

    @Builder
    public AddTableCountRequestDto(String userId, String businessName, int tableCount) {
        this.userId = userId;
        this.businessName = businessName;
        this.tableCount = tableCount;
    }

    public TableCount toTableCountEntity() {
        return TableCount.builder()
                .tableCountId(identityCode + RandomIdCreator.create())
                .userId(this.userId)
                .businessName(this.businessName)
                .tableCount(this.tableCount)
                .build();
    }
}
