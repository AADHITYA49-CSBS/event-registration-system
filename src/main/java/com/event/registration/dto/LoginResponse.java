package com.event.registration.dto;

public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse(String token) {
        this.accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getToken() {
        return accessToken;
    }

    public String getType() {
        return tokenType;
    }
}
