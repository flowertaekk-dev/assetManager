package com.assetManager.server.controller.account.dto;

import com.assetManager.server.domain.account.Account;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveAccountRequestDto {

    private String businessId;
    private String contents;

    @Builder
    public SaveAccountRequestDto(String businessId, String contents) {
        this.businessId = businessId;
        this.contents = contents;
    }

    public Account toAccountEntity() {
        return Account.builder()
                .accountId(RandomIdCreator.createAccountId())
                .businessId(this.businessId)
                .contents(this.contents)
                .build();
    }
}
