package com.assetManager.server.domain.business;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByBusinessName(String businessName);

    Optional<Business> findByUserIdAndBusinessName(String userId, String businessName);

}
