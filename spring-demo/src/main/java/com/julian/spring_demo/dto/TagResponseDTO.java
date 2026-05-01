package com.julian.spring_demo.dto;

public class TagResponseDTO {

    private Long id;
    private String name;
    private int totalProducts;

    public TagResponseDTO () {}

    public TagResponseDTO (Long id, String name, int totalProducts) {
        this.id = id;
        this.name = name;
        this.totalProducts = totalProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalProducts() {
        return totalProducts;
    }
}
