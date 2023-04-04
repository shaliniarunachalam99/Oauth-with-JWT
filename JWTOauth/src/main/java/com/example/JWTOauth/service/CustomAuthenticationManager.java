//package com.example.JWTOauth.service;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class CustomAuthenticationManager implements AuthenticationManager {
//    private final List<AuthenticationProvider> authenticationProviders;
//
//    public CustomAuthenticationManager(List<AuthenticationProvider> authenticationProviders) {
//        this.authenticationProviders = authenticationProviders;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) {
//        ProviderManager authenticationManager = new ProviderManager(authenticationProviders);
//        Authentication result = authenticationManager.authenticate(authentication);
//        return result;
//    }
//}
