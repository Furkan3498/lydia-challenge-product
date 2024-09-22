package com.lydiasystems.challenge.security;


import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest(String testUser, String testPassword) {
    }

    public AuthenticationRequest() {

    }
}

