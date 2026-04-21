package com.julian.spring_demo.service;

import com.julian.spring_demo.exception.CategoryNotFoundException;
import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.repository.CategoryRepository;
import com.julian.spring_demo.repository.ProductRepository;
import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Product> getAll (String name, Double maxPrice, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        if (name != null) return repository.findByNameContainingIgnoreCase(name, pageable);
        return repository.findAll(pageable);
    }

    public Product getById (Long id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return repository.findByCategoryId(categoryId, pageable);
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
