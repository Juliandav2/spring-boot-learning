package com.julian.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;

public class TagRequestDTO {

    @NotBlank (message = "Name cannot be blank")
    private String name;

    public TagRequestDTO () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
