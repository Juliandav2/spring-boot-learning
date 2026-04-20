package com.julian.spring_demo.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive (message = "Price must be positive")
    private double price;

    @PositiveOrZero (message = "Stock cannot be negative")
    private int stock;

    public Product () {}

    public Product (Long id, String name, double price, int stock) {

        this.id = id;

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name cannot be null or blank");
        }

        this.name = name;

        if (price <= 0) {
            throw new IllegalArgumentException("The price cannot be negative or 0");
        }

        this.price = price;

        if (stock < 0) {
            throw new IllegalArgumentException("The stock cannot be negative");
        }

        this.stock = stock;
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
}
