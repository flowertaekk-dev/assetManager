package com.assetManager.server.controller.setting.table.dto;

import com.assetManager.server.domain.tableCount.TableCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadTableCountRequestDto {

    private String userId;
    private String businessName;

    @Builder
    public ReadTableCountRequestDto(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }

    public TableCount toTableCountEntity() {
        return TableCount.builder()
                .userId(this.userId)
                .businessName(this.businessName)
                .build();
    }
}
