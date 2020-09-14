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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessName;

    @Builder
    public Business(String userId, String businessName) {
        this.userId = userId;
        this.businessName = businessName;
    }

    // -----------------------------------------------------------------------------------
    // util method

    /**
     * 상호명을 변경
     */
    public void updateBusinessName(String newBusinessName) {
        this.businessName = newBusinessName;
    }
}
