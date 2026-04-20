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

    public List<Product> getAll () {
        return repository.finAll();
    }

    public Product getById (Long id) {

        Product product = repository.findById(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;

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
