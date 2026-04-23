package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.ProductRequestDTO;
import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.exception.CategoryNotFoundException;
import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.repository.CategoryRepository;
import com.julian.spring_demo.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductService (ProductRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional (readOnly = true)
    public Page<ProductResponseDTO> getAll (String name, Double maxPrice, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy.trim()));
        Page<Product> products;
        if (name != null) {
            products = repository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            products = repository.findAll(pageable);
        }
        return products.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getById (Long id) {
        return toDTO(repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public Page<ProductResponseDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        return repository.findByCategoryId(categoryId, pageable).map(this::toDTO);
    }

    @Transactional
    public ProductResponseDTO create (ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            product.setCategory(category);
        }
        return toDTO(repository.save(product));
    }

    @Transactional
    public ProductResponseDTO update (Long id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            product.setCategory(category);
        }
        return toDTO(repository.save(product));
    }

    @Transactional
    public void delete (Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private ProductResponseDTO toDTO (Product product) {
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        return new ProductResponseDTO (
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                categoryName
        );
    }
}
