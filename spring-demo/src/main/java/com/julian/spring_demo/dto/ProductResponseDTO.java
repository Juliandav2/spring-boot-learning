package com.julian.spring_demo.dto;

import java.util.Set;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private double price;
    private int stock;
    private String categoryName;
    private Set<String> tags;

    public ProductResponseDTO () {}

    public ProductResponseDTO (Long id, String name, double price, int stock, String categoryName, Set<String> tags) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
        this.tags = tags;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<String> getTags() {
        return tags;
    }
}
