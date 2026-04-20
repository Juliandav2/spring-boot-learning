package com.julian.spring_demo.repository;

import com.julian.spring_demo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();
    private Long nextId = 1L;

    public List<Product> finAll () {
        return new ArrayList<>(products.values());
    }

    public Product findById (Long id) {
        return products.get(id);
    }

    public Product save (Product product) {

        if (product.getId() == null) {
            product.setId(nextId++);

        }

        products.put(product.getId(), product);
        return product;

    }

    public void deleteById (Long id) {
        products.remove(id);
    }

    public boolean existsById (Long id) {
        return !products.containsKey(id);
    }

}
