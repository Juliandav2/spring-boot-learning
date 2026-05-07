package com.julian.spring_demo.service;

import com.julian.spring_demo.dto.ProductRequestDTO;
import com.julian.spring_demo.dto.ProductResponseDTO;
import com.julian.spring_demo.exception.CategoryNotFoundException;
import com.julian.spring_demo.exception.ProductNotFoundException;
import com.julian.spring_demo.exception.TagNotFoundException;
import com.julian.spring_demo.model.Category;
import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.model.Tag;
import com.julian.spring_demo.repository.CategoryRepository;
import com.julian.spring_demo.repository.ProductRepository;
import com.julian.spring_demo.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService (ProductRepository repository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
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
        log.debug("Fetching product with id: {}", id);
        return toDTO(repository.findById(id).orElseThrow(() -> {log.warn("Product not found with id: {}", id);
            return new ProductNotFoundException(id);
        }));
    }

    public Page<ProductResponseDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        return repository.findByCategoryId(categoryId, pageable).map(this::toDTO);
    }

    @Transactional
    public ProductResponseDTO create (ProductRequestDTO dto) {
        log.info("Creating product with name {}", dto.getName());
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> {log.warn("Category not found with id: {}", dto.getCategoryId());
                        return new CategoryNotFoundException(dto.getCategoryId());
                    });
            product.setCategory(category);
        }
        ProductResponseDTO result = toDTO(repository.save(product));
        log.info("Product created successfully with id: {}", result.getId());
        return result;
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
       Product product = repository.findById(id)
               .orElseThrow(() -> new ProductNotFoundException(id));
       product.setDeleted(true);
       repository.save(product);
       log.info("Product soft deleted with id: {}", id);
    }

    @Transactional (readOnly = true)
    public List<ProductResponseDTO> getByPriceRange (double minPrice, double maxPrice) {
        return repository.findByPriceRange(minPrice, maxPrice)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional (readOnly = true)
    public List<ProductResponseDTO> getLowStock (int minStock) {
        return  repository.findLowStock(minStock)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO addTag (Long productId, Long tagId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        product.getTags().add(tag);
        return toDTO(repository.save(product));
    }

    @Transactional
    public ProductResponseDTO removeTag (Long productId, Long tagId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        product.getTags().remove(tag);
        return toDTO(repository.save(product));
    }

    private ProductResponseDTO toDTO (Product product) {
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        Set<String> tags = product.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
        return new ProductResponseDTO (
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                categoryName,
                tags,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
