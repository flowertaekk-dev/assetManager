package com.assetManager.server.controller.account.dto;

import com.assetManager.server.domain.account.Account;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveAccountRequestDto {
    private final String identityCode = "AC-";

    private String businessId;
    private String contents;

    @Builder
    public SaveAccountRequestDto(String businessId, String contents) {
        this.businessId = businessId;
        this.contents = contents;
    }

    public Account toAccountEntity() {
        return Account.builder()
                .accountId(identityCode + RandomIdCreator.create())
                .businessId(this.businessId)
                .contents(this.contents)
                .build();
    }
}
