package com.example.JWTOauth.security;

import com.example.JWTOauth.entity.Client;
import com.example.JWTOauth.repository.ClientRepository;
import com.example.JWTOauth.repository.CustomClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
public class CustomClientRepoImpl implements CustomClientRepo {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ClientRegistration findByRegistrationId(String registrationId) {
        Client client = clientRepository.findByRegistrationId(registrationId);
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(client.getRegistrationId())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri("http://localhost:8080/login/oauth2/code/custom")
                .authorizationUri("https://example.com/oauth2/authorize")
                .tokenUri("https://example.com/oauth2/token")
                .build();
        return clientRegistration;
    }


}
