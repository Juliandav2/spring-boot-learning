package com.julian.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    @NotBlank (message = "Email cannot be blank")
    private String email;

    @NotBlank (message = "Password cannot be blank")
    private String password;

    public LoginRequestDTO () {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
