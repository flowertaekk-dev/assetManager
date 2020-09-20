package com.assetManager.server.domain.tableCount;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TableCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private int tableCount;

    @Builder
    public TableCount(String userId, String businessName, int tableCount) {
        this.userId = userId;
        this.businessName = businessName;
        this.tableCount = tableCount;
    }

    // -----------------------------------------------------------------------------------
    // util method

    public void updateTableCount(int tableCount) {
        this.tableCount = tableCount;
    }
}
