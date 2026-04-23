package com.julian.spring_demo.dto;

import jakarta.validation.constraints.*;

public class ProductRequestDTO {

    @NotBlank (message = "Name cannot be blank")
    private String name;

    @Positive (message = "Price must be positive")
    private double price;

    @PositiveOrZero (message = "Stock cannot be negative")
    private int stock;

    private Long categoryId;

    public ProductRequestDTO () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
