package com.julian.spring_demo.events;

import com.julian.spring_demo.model.Product;

public class ProductCreatedEvent {

    private final Product product;

    public ProductCreatedEvent (Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
