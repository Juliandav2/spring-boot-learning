package com.julian.spring_demo.service;

import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService (ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll (String name, Double maxPrice) {
        if (name != null) return repository.findByNameContainingIgnoreCase(name);
        if (maxPrice != null) return repository.findByPriceLessThanEqual(maxPrice);
        return repository.findAll();
    }

    public Product getById (Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create (Product product) {
        return repository.save(product);
    }

    public Product update (Long id, Product product) {
        if (repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        product.setId(id);
        return repository.save(product);
    }

    public void delete (Long id) {
        if (repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

}
