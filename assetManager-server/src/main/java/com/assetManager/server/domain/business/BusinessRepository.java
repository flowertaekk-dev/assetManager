package com.assetManager.server.domain.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, String> {

    Optional<Business> findByUserIdAndBusinessName(String userId, String businessName);

    Optional<Business> findByUserIdAndBusinessId(String userId, String businessId);

    List<Business> findByUserId(String userId);

    void deleteByUserId(String userId);
}
