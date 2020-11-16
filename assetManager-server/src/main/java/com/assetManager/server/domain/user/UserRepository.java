package com.assetManager.server.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByIdAndPassword(String id, String password);

    Optional<User> findByEmail(String email);

    @Query("select u.salt from User u where u.id = ?1")
    Optional<String> findSaltById(String id);
}
