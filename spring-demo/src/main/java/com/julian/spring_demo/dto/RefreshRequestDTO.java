package com.julian.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequestDTO {

    @NotBlank (message = "Refresh token cannot be blank")
    private String refreshToken;

    public RefreshRequestDTO () {}

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
