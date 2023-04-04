package com.example.JWTOauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private CustomClientRepoImpl customClientRepo;

    @Override
    public ClientDetails loadClientByClientId(String registrationId) throws ClientRegistrationException {
        ClientRegistration client = customClientRepo.findByRegistrationId(registrationId);
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(client.getClientId());
        baseClientDetails.setClientSecret(client.getClientSecret());

        return baseClientDetails;
    }
}
