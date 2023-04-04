package com.example.JWTOauth.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private String registrationId;
    private String clientId;
    private String clientSecret;
}
