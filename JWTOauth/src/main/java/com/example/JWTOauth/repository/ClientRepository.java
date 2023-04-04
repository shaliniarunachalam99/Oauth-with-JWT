package com.example.JWTOauth.repository;

import com.example.JWTOauth.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByRegistrationId(String registrationId);

}
