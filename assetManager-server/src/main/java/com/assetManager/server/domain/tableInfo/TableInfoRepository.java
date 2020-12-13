package com.assetManager.server.domain.tableInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableInfoRepository extends JpaRepository<TableInfo, String> {

    Optional<TableInfo> findByUserIdAndBusinessId(String userId, String businessId);

    void deleteByUserId(String userId);
}
