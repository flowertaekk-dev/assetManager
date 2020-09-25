package com.assetManager.server.domain.tableInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TableInfo {

    @Id
    private String tableInfoId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessId;

    @Column(nullable = false)
    private int tableCount;

    @Builder
    public TableInfo(String tableInfoId, String userId, String businessId, int tableCount) {
        this.tableInfoId = tableInfoId;
        this.userId = userId;
        this.businessId = businessId;
        this.tableCount = tableCount;
    }

    // -----------------------------------------------------------------------------------
    // util method

    /**
     * 테이블 개수를 수정한다.
     *
     * @return 수정된 TableInfo 객체
     */
    public TableInfo updateTableCount(int tableCount) {
        this.tableCount = tableCount;
        return this;
    }
}
