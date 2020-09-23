package com.assetManager.server.controller.setting.business.dto;

import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.tableCount.TableCount;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddBusinessRequestDto {

    private final String IdentityCode = "BS-";

    private String userId;
    private String businessName;

    @Builder
    public AddBusinessRequestDto(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }

    public Business toBusinessEntity() {
        return Business.builder()
                .businessId(IdentityCode + RandomIdCreator.create())
                .userId(this.userId)
                .businessName(this.businessName)
                .build();
    }

    public TableCount toTableCountEntity() {
        return TableCount.builder()

                .build();
    }
}
