package com.example.JWTOauth.repository;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

public interface CustomClientRepo extends ClientRegistrationRepository {
    ClientRegistration findByRegistrationId(String registrationId);

}
