package com.example.JWTOauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {
    private String registrationId;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
}
