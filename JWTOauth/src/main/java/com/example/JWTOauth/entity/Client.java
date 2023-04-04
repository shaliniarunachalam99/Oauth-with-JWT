package com.example.JWTOauth.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client_details")
public class Client {

    @Id
    private String registrationId;
    private String clientId;
    private String clientSecret;

}
