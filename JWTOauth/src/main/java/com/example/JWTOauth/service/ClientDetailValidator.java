package com.example.JWTOauth.service;

import com.example.JWTOauth.dto.Login;
import com.example.JWTOauth.entity.User;
import com.example.JWTOauth.repository.ClientRepository;
import com.example.JWTOauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ClientDetailValidator {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validateClientDetails(Login login) {
        String registrationId = login.getRegistrationId();
        String currentUserName = login.getUserName();
        String rawPassword = login.getPassword();
        String currentClientId = login.getClientId();


        User user = userRepository.findByClientClientIdAndUsername(currentClientId, currentUserName);
        String clientId = user.getClient().getClientId();

        String encryptedPassword = user.getPassword();

        boolean isMatch = passwordEncoder.matches(rawPassword, encryptedPassword);

        if (currentClientId.equalsIgnoreCase(clientId) && currentUserName.equalsIgnoreCase(user.getUsername()) && isMatch) {
            return true;
        } else {
            return false;
        }
    }

}
