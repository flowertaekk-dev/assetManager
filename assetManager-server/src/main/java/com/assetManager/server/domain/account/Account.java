package com.assetManager.server.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Getter
@NoArgsConstructor
@Entity
public class Account {

    @Id
    private String accountId;

    @Column(nullable = false)
    private String businessId;

    @Lob
    @Column(nullable = false)
    private String contents;

    @Builder
    public Account(String accountId, String businessId, String contents) {
        this.accountId = accountId;
        this.businessId = businessId;
        this.contents = contents;
    }
}
