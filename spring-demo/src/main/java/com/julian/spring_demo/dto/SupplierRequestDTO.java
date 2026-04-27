package com.julian.spring_demo.dto;

import jakarta.validation.constraints.*;

public class SupplierRequestDTO {

    @NotBlank (message = "Name cannot be blank")
    private String name;

    @NotBlank (message = "Email cannot be blank")
    private String email;

    @NotBlank (message = "Phone cannot be blank")
    private String phone;

    public SupplierRequestDTO () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
