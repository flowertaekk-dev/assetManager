package com.assetManager.server.domain.tableCount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableCountRepository extends JpaRepository<TableCount, Long> {

    Optional<TableCount> findByUserIdAndBusinessName(String userId, String businessName);

}
