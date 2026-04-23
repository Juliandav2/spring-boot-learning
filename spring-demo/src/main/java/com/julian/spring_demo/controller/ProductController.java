package com.julian.spring_demo.controller;

import com.julian.spring_demo.dto.ProductRequestDTO;
import com.julian.spring_demo.dto.ProductResponseDTO;
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
    public ResponseEntity<ProductResponseDTO> getById (@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll (@RequestParam (required = false) String name,
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
    public ResponseEntity<ProductResponseDTO> create (@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ProductResponseDTO> update (@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

