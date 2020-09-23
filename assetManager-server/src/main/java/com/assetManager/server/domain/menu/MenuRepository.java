package com.assetManager.server.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {

    Optional<Menu> findByUserIdAndBusinessNameAndMenu(String userId, String businessName, String menu);

    List<Menu> findByUserIdAndBusinessName(String userId, String businessName);

}
