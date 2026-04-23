package com.julian.spring_demo.service;

import com.julian.spring_demo.exception.CategoryNotFoundException;
import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService (CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Category> getAll () {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getById (Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Transactional
    public Category create (Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update (Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete (Long id) {
        if (categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }
}
