package com.company.storemanager.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String fullName;
    private String email;
    private String message;

    public AuthResponse(String token, String username, String fullName, String email, String message) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.message = message;
    }

    public AuthResponse(String message) {
        this.message = message;
    }
}