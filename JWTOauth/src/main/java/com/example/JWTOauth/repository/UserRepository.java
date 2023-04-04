package com.example.JWTOauth.repository;

import com.example.JWTOauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    User findByClientClientIdAndUsername(String registrationId, String currentUserName);
}
