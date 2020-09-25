package com.assetManager.server.domain.tableCount;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TableCount { // TODO 이거 TableInfo로 바꾸자

    @Id
    private String tableCountId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessId;

    @Column(nullable = false)
    private int tableCount;

    @Builder
    public TableCount(String tableCountId, String userId, String businessId, int tableCount) {
        this.tableCountId = tableCountId;
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
    public TableCount updateTableCount(int tableCount) {
        this.tableCount = tableCount;
        return this;
    }
}
