package com.julian.spring_demo.controller;

import com.julian.spring_demo.model.Product;
import com.julian.spring_demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController (ProductService productService) {
        this.productService = productService;
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Product> getById (@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAll (@RequestParam (required = false) String name,
                                                 @RequestParam (required = false) Double maxPrice,
                                                 @RequestParam (required = false) Long categoryId,
                                                 @RequestParam (defaultValue = "0") int page,
                                                 @RequestParam (defaultValue = "10") int size,
                                                 @RequestParam (defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy.trim()));
        if (categoryId != null) return ResponseEntity.ok(productService.findByCategoryId(categoryId, pageable));
        return ResponseEntity.ok(productService.getAll(name, maxPrice, page, size, sortBy));
    }

    @PostMapping
    public ResponseEntity<Product> create (@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Product> update (@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

