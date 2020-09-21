package com.assetManager.server.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByUserIdAndBusinessNameAndMenu(String userId, String businessName, String menu);

}
