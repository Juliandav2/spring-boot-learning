package com.julian.spring_demo.service;

import com.julian.spring_demo.exception.CategoryNotFoundException;
import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.repository.CategoryRepository;
import com.julian.spring_demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductService (ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAll (String name, Double maxPrice) {
        if (name != null) return repository.findByNameContainingIgnoreCase(name);
        if (maxPrice != null) return repository.findByPriceLessThanEqual(maxPrice);
        return repository.findAll();
    }

    public Product getById (Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findByCategoryId(Long categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    public Product create (Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException(product.getCategory().getId()));
            product.setCategory(category);
        }
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
