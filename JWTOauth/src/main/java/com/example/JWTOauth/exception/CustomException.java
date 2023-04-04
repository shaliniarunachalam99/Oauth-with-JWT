package com.example.JWTOauth.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {
    private String message;
    private Integer responseCode;

    public CustomException(String message) {
        super(message);
    }
    public CustomException(String message, Integer responseCode) {
        this.message = message;
        this.responseCode = responseCode;
    }
}
