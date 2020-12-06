package com.assetManager.server.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {

    Optional<Menu> findByUserIdAndBusinessIdAndMenu(String userId, String businessId, String menu);

    void deleteByUserId(String userId);

    List<Menu> findByUserIdAndBusinessId(String userId, String businessId);
}
