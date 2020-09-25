package com.assetManager.server.domain.business;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Business {

    @Id
    private String businessId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessName;

    @Builder
    public Business(String businessId, String userId, String businessName) {
        this.businessId = businessId;
        this.userId = userId;
        this.businessName = businessName;
    }

    // -----------------------------------------------------------------------------------
    // util method

    /**
     * 상호명을 변경
     */
    public Business updateBusinessName(String businessName) {
        this.businessName = businessName;
        return this;
    }
}
