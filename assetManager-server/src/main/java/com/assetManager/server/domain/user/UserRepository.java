package com.assetManager.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndPassword(String id, String password);

    Optional<User> findByEmail(String email);

}
